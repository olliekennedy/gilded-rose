package com.gildedrose

import kotlin.math.max
import kotlin.math.min

const val BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert"
const val SULFURAS = "Sulfuras, Hand of Ragnaros"
const val AGED_BRIE = "Aged Brie"
const val CONJURED = "Conjured"

class GildedRose(val items: List<Item>) {

    fun updateQuality() {
        items.forEach { updateQualityFor(it) }
    }

    private fun updateQualityFor(item: Item) {
        if (item.name == SULFURAS) return

        item.sellIn -= 1

        val degradationAmount = calculateDegradationRateFor(item)

        when (item.name) {
            AGED_BRIE -> {
                item.quality = item.quality.increase(upTo = 50, by = degradationAmount)
            }

            BACKSTAGE_PASSES -> {
                item.quality = updatedQualityForBackstagePasses(item)
            }

            else -> {
                item.quality = item.quality.decrease(downTo = 0, by = degradationAmount)
            }
        }
    }

    private fun calculateDegradationRateFor(item: Item): Int {
        val conjuredMultiplier = if (item.name.startsWith(CONJURED)) 2 else 1
        val passedSellByDateMultiplier = if (item.sellIn < 0) 2 else 1

        return conjuredMultiplier * passedSellByDateMultiplier
    }

    private fun updatedQualityForBackstagePasses(item: Item) =
        when {
            item.sellIn < 0 -> 0
            item.sellIn < 5 -> {
                item.quality.increase(upTo = 50, by = 3)
            }

            item.sellIn in 5..<10 -> {
                item.quality.increase(upTo = 50, by = 2)
            }

            else -> {
                item.quality.increase(upTo = 50, by = 1)
            }
        }
}

private fun Int.increaseWithLimit(increase: Int, limit: Int): Int =
    min(this + increase, limit)

private fun Int.decrease(downTo: Int, by: Int): Int =
    max(this - by, downTo)

