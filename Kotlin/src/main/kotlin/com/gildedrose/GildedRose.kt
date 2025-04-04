package com.gildedrose

private const val BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert"
private const val SULFURAS = "Sulfuras, Hand of Ragnaros"
private const val AGED_BRIE = "Aged Brie"

class GildedRose(val items: List<Item>) {

    fun updateQuality() {
        items.forEach { item ->
            if (item.name != SULFURAS) {
                item.sellIn -= 1
            }

            when (item.name) {
                AGED_BRIE -> {
                    if (item.quality < 50) {
                        item.quality += 1
                    }
                }
                BACKSTAGE_PASSES -> {
                    item.quality = updatedQualityForBackstagePasses(item)

                    if (item.quality >= 50) {
                        item.quality = 50
                    }
                }
                SULFURAS -> {}
                else -> {
                    if (item.quality > 0) {
                        item.quality -= 1
                    }
                }
            }

            if (item.sellIn < 0) {
                when (item.name) {
                    BACKSTAGE_PASSES -> {
                        item.quality = 0
                    }
                    AGED_BRIE -> {
                        if (item.quality < 50) {
                            item.quality += 1
                        }
                    }
                    SULFURAS -> {}
                    else -> {
                        if (item.quality > 0) {
                            item.quality -= 1
                        }
                    }
                }
            }
        }
    }

    private fun updatedQualityForBackstagePasses(item: Item) = when {
        item.sellIn < 5 -> item.quality + 3
        item.sellIn in 5..<10 -> item.quality + 2
        else -> item.quality + 1
    }
}

