package com.gildedrose

import kotlin.math.max
import kotlin.math.min

const val BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert"
const val SULFURAS = "Sulfuras, Hand of Ragnaros"
const val AGED_BRIE = "Aged Brie"
const val CONJURED = "Conjured"

class GildedRose(val items: List<Item>) {

    fun updateQuality() {
        items = items
            .map { GildedRoseItem.from(it) }
            .map { updateQualityFor(it) }
            .map { Item(it.name, it.sellIn, it.quality) }
    }

    private fun updateQualityFor(item: GildedRoseItem): GildedRoseItem {
        if (item.name == SULFURAS) return item

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

        return item
    }

    private fun calculateDegradationRateFor(item: GildedRoseItem): Int {
        val conjuredMultiplier = if (item.name.startsWith(CONJURED)) 2 else 1
        val passedSellByDateMultiplier = if (item.sellIn < 0) 2 else 1

        return conjuredMultiplier * passedSellByDateMultiplier
    }

    private fun updatedQualityForBackstagePasses(item: GildedRoseItem) =
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

class GildedRoseItem(
    val name: String,
    var sellIn: Int,
    var quality: Int,
) {
    companion object {
        fun from(it: Item) = GildedRoseItem(it.name, it.sellIn, it.quality)
    }
}

private fun Int.increase(upTo: Int, by: Int): Int =
    min(this + by, upTo)

private fun Int.decrease(downTo: Int, by: Int): Int =
    max(this - by, downTo)

