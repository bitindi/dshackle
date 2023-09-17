package io.emeraldpay.dshackle.upstream.forkchoice

import io.emeraldpay.dshackle.data.BlockContainer
import io.emeraldpay.dshackle.data.BlockId
import spock.lang.Specification

import java.time.Instant

class MostWorkForkChoiceSpec extends Specification {

    def blocks = [1L, 2, 3, 4].collect { i ->
        byte[] hash = new byte[32]
        hash[0] = i as byte
        new BlockContainer(i, BlockId.from(hash), BigInteger.valueOf(i), Instant.now(), false, null, null, BlockId.from(hash), [], 0, "MostWorkForkChoiceSpec")
    }

    def "filters blocks"() {
        def choice = new MostWorkForkChoice()
        choice.choose(blocks[1])
        expect:
        !choice.filter(blocks[0])
        choice.filter(blocks[2])
    }

    def "chooses correct block as head"() {
        def choice = new MostWorkForkChoice()
        choice.choose(blocks[1])
        when:
        choice.choose(blocks[0])
        then:
        choice.getHead() == blocks[1]
        when:
        choice.choose(blocks[2])
        then:
        choice.getHead() == blocks[2]
    }
}
