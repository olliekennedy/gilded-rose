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

        val degradationMultiplier = calculateDegradationRateFor(item)

        when (item.name) {
            AGED_BRIE -> {
                repeat(degradationMultiplier) {
                    item.quality = item.quality.increaseWithLimit(1, 50)
                }
            }

            BACKSTAGE_PASSES -> {
                item.quality = updatedQualityForBackstagePasses(item)
            }

            else -> {
                repeat(degradationMultiplier) {
                    item.quality = item.quality.decreaseWithLimit(1, 0)
                }
            }
        }
    }

    private fun calculateDegradationRateFor(item: Item): Int {
        val conjuredMultiplier = if (item.name.startsWith(CONJURED)) 2 else 1
        val passedSellByDateMultiplier = if (item.sellIn < 0) 2 else 1

        val degradationMultiplier = conjuredMultiplier * passedSellByDateMultiplier
        return degradationMultiplier
    }

    private fun updatedQualityForBackstagePasses(item: Item) = when {
        item.sellIn < 0 -> 0
        item.sellIn < 5 -> item.quality.increaseWithLimit(3, 50)
        item.sellIn in 5..<10 -> item.quality.increaseWithLimit(2, 50)
        else -> item.quality.increaseWithLimit(1, 50)
    }
}

private fun Int.increaseWithLimit(increase: Int, limit: Int): Int =
    min(this + increase, limit)

private fun Int.decreaseWithLimit(decrease: Int, limit: Int): Int =
    max(this - decrease, limit)

