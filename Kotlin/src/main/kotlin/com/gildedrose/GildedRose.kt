package com.gildedrose

private const val BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert"
private const val SULFURAS = "Sulfuras, Hand of Ragnaros"
private const val AGED_BRIE = "Aged Brie"

class GildedRose(val items: List<Item>) {

    fun updateQuality() {
        items.forEach { item ->
            if (item.name == AGED_BRIE) {
                if (item.quality < 50) {
                    item.quality += 1

                    if (item.name == BACKSTAGE_PASSES) {
                        if (item.sellIn < 11) {
                            if (item.quality < 50) {
                                item.quality += 1
                            }
                        }

                        if (item.sellIn < 6) {
                            if (item.quality < 50) {
                                item.quality += 1
                            }
                        }
                    }
                }
            } else if (item.name == BACKSTAGE_PASSES) {
                if (item.quality < 50) {
                    item.quality += 1

                    if (item.name == BACKSTAGE_PASSES) {
                        if (item.sellIn < 11) {
                            if (item.quality < 50) {
                                item.quality += 1
                            }
                        }

                        if (item.sellIn < 6) {
                            if (item.quality < 50) {
                                item.quality += 1
                            }
                        }
                    }
                }
            } else if (item.name != SULFURAS) {
                if (item.quality > 0) {
                    item.quality -= 1
                }
            }

            if (item.name != SULFURAS) {
                item.sellIn -= 1
            }

            if (item.sellIn < 0) {
                if (item.name != AGED_BRIE) {
                    if (item.name != BACKSTAGE_PASSES) {
                        if (item.quality > 0) {
                            if (item.name != SULFURAS) {
                                item.quality -= 1
                            }
                        }
                    } else {
                        item.quality = 0
                    }
                } else {
                    if (item.quality < 50) {
                        item.quality += 1
                    }
                }
            }
        }
    }

}

