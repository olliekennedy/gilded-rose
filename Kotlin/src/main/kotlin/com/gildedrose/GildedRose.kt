package com.gildedrose

private const val BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert"
private const val SULFURAS = "Sulfuras, Hand of Ragnaros"
private const val AGED_BRIE = "Aged Brie"

class GildedRose(val items: List<Item>) {

    fun updateQuality() {
        items.forEach { item ->
            if (item.name == SULFURAS) return@forEach

            item.sellIn -= 1

            if (item.name == BACKSTAGE_PASSES) {
                item.quality = updatedQualityForBackstagePasses(item)
                return@forEach
            }

            val degradationMultiplier = if (item.sellIn < 0) 2 else 1

            repeat(degradationMultiplier) {
                when (item.name) {
                    AGED_BRIE -> {
                        item.quality = item.quality.increaseWithLimit(1, 50)
                    }
                    else -> {
                        item.quality = item.quality.decreaseWithLimit(1, 0)
                    }
                }
            }
        }
    }

    private fun updatedQualityForBackstagePasses(item: Item) = when {
        item.sellIn < 0 -> 0
        item.sellIn < 5 -> item.quality.increaseWithLimit(3, 50)
        item.sellIn in 5..<10 -> item.quality.increaseWithLimit(2, 50)
        else -> item.quality.increaseWithLimit(1, 50)
    }
}

private fun Int.increaseWithLimit(increase: Int, limit: Int): Int =
    this.plus(increase).takeIf { it < limit } ?: limit

private fun Int.decreaseWithLimit(decrease: Int, limit: Int): Int =
    this.minus(decrease).takeIf { it > limit } ?: limit

