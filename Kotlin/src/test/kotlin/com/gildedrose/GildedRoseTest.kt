package com.gildedrose

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GildedRoseTest {

    @Test
    fun foo() {
        val items = listOf(Item("foo", 0, 0))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("foo", app.items[0].name)
    }

    @Test
    fun `sulfuras does not change in quality or sellin`() {
        val items = listOf(
            Item(SULFURAS, 10, 80),
        )
        val app = GildedRose(items)

        repeat(37) {
            app.updateQuality()
        }

        assertEquals(items[0].name, app.items[0].name)
        assertEquals(items[0].quality, app.items[0].quality)
        assertEquals(items[0].sellIn, app.items[0].sellIn)
    }

    @Test
    fun `a normal item degrades at the normal rate`() {
        val items = listOf(
            Item("Banana", 5, 30),
        )
        val app = GildedRose(items)

        repeat(2) {
            app.updateQuality()
        }

        assertThat(app.items[0].name, equalTo("Banana"))
        assertThat(app.items[0].sellIn, equalTo(3))
        assertThat(app.items[0].quality, equalTo(28))
    }

    @Test
    fun `an item past its sell by date degrades at double rate`() {
        val items = listOf(
            Item("Banana", -2, 30),
        )
        val app = GildedRose(items)

        repeat(2) {
            app.updateQuality()
        }

        assertThat(app.items[0].name, equalTo("Banana"))
        assertThat(app.items[0].sellIn, equalTo(-4))
        assertThat(app.items[0].quality, equalTo(26))
    }

    @Test
    fun `brie increases in quality`() {
        val items = listOf(
            Item(AGED_BRIE, 5, 30),
        )
        val app = GildedRose(items)

        repeat(2) {
            app.updateQuality()
        }

        assertThat(app.items[0].name, equalTo(AGED_BRIE))
        assertThat(app.items[0].sellIn, equalTo(3))
        assertThat(app.items[0].quality, equalTo(32))
    }

    @Test
    fun `brie after sell by date increases in quality at double the rate`() {
        val items = listOf(
            Item(AGED_BRIE, -2, 30),
        )
        val app = GildedRose(items)

        repeat(2) {
            app.updateQuality()
        }

        assertThat(app.items[0].name, equalTo(AGED_BRIE))
        assertThat(app.items[0].sellIn, equalTo(-4))
        assertThat(app.items[0].quality, equalTo(34))
    }

    @Test
    fun `quality does not drop below zero`() {
        val items = listOf(
            Item("Banana", -4, 1),
            Item("Banana", -4, 2),
        )
        val app = GildedRose(items)

        app.updateQuality()

        assertThat(app.items[0].name, equalTo("Banana"))
        assertThat(app.items[0].sellIn, equalTo(-5))
        assertThat(app.items[0].quality, equalTo(0))

        assertThat(app.items[1].name, equalTo("Banana"))
        assertThat(app.items[1].sellIn, equalTo(-5))
        assertThat(app.items[1].quality, equalTo(0))
    }

    @Test
    fun `quality does increase beyond 50`() {
        val items = listOf(
            Item(AGED_BRIE, -4, 48),
            Item(AGED_BRIE, -4, 49),
            Item(BACKSTAGE_PASSES, 30, 50),
        )
        val app = GildedRose(items)

        app.updateQuality()

        assertThat(app.items[0].name, equalTo(AGED_BRIE))
        assertThat(app.items[0].sellIn, equalTo(-5))
        assertThat(app.items[0].quality, equalTo(50))

        assertThat(app.items[1].name, equalTo(AGED_BRIE))
        assertThat(app.items[1].sellIn, equalTo(-5))
        assertThat(app.items[1].quality, equalTo(50))

        assertThat(app.items[2].name, equalTo(BACKSTAGE_PASSES))
        assertThat(app.items[2].sellIn, equalTo(29))
        assertThat(app.items[2].quality, equalTo(50))
    }

    @Test
    fun `backstage passes are useless after the concert`() {
        val items = listOf(
            Item(BACKSTAGE_PASSES, 0, 30),
        )
        val app = GildedRose(items)

        app.updateQuality()

        assertThat(app.items[0].name, equalTo(BACKSTAGE_PASSES))
        assertThat(app.items[0].sellIn, equalTo(-1))
        assertThat(app.items[0].quality, equalTo(0))
    }

    @Test
    fun `backstage passes increase in quality by 3 when concert is imminent`() {
        val items = listOf(
            Item(BACKSTAGE_PASSES, 1, 30),
            Item(BACKSTAGE_PASSES, 5, 30),
        )
        val app = GildedRose(items)

        app.updateQuality()

        assertThat(app.items[0].name, equalTo(BACKSTAGE_PASSES))
        assertThat(app.items[0].sellIn, equalTo(0))
        assertThat(app.items[0].quality, equalTo(33))

        assertThat(app.items[1].name, equalTo(BACKSTAGE_PASSES))
        assertThat(app.items[1].sellIn, equalTo(4))
        assertThat(app.items[1].quality, equalTo(33))
    }

    @Test
    fun `backstage passes increase in quality by 2 when concert is close`() {
        val items = listOf(
            Item(BACKSTAGE_PASSES, 6, 30),
            Item(BACKSTAGE_PASSES, 10, 30),
        )
        val app = GildedRose(items)

        app.updateQuality()

        assertThat(app.items[0].name, equalTo(BACKSTAGE_PASSES))
        assertThat(app.items[0].sellIn, equalTo(5))
        assertThat(app.items[0].quality, equalTo(32))

        assertThat(app.items[1].name, equalTo(BACKSTAGE_PASSES))
        assertThat(app.items[1].sellIn, equalTo(9))
        assertThat(app.items[1].quality, equalTo(32))
    }

    @Test
    fun `backstage passes increase in quality by 1 when concert is far away`() {
        val items = listOf(
            Item(BACKSTAGE_PASSES, 11, 30),
            Item(BACKSTAGE_PASSES, 200, 30),
        )
        val app = GildedRose(items)

        app.updateQuality()

        assertThat(app.items[0].name, equalTo(BACKSTAGE_PASSES))
        assertThat(app.items[0].sellIn, equalTo(10))
        assertThat(app.items[0].quality, equalTo(31))

        assertThat(app.items[1].name, equalTo(BACKSTAGE_PASSES))
        assertThat(app.items[1].sellIn, equalTo(199))
        assertThat(app.items[1].quality, equalTo(31))
    }
}


