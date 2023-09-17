package io.emeraldpay.dshackle.upstream.forkchoice

import io.emeraldpay.dshackle.data.BlockContainer
import java.util.concurrent.atomic.AtomicReference

class MostWorkForkChoice : ForkChoice {
    private val head = AtomicReference<BlockContainer>(null)

    override fun getHead(): BlockContainer? {
        return head.get()
    }

    override fun filter(block: BlockContainer): Boolean {
        val curr = head.get()
        return curr == null || curr.difficulty < block.difficulty
    }

    override fun choose(block: BlockContainer): ForkChoice.ChoiceResult {
        val nwhead = head.updateAndGet { curr ->
            if (filter(block)) {
                block
            } else {
                curr
            }
        }
        if (nwhead.hash == block.hash) {
            return ForkChoice.ChoiceResult.Updated(nwhead)
        }
        return ForkChoice.ChoiceResult.Same(nwhead)
    }
}
