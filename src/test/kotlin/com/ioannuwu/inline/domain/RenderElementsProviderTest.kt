package com.ioannuwu.inline.domain

import com.ioannuwu.inline.data.DefaultSettings
import com.ioannuwu.inline.data.EffectType
import com.ioannuwu.inline.data.FontData
import com.ioannuwu.inline.domain.elements.RenderElementKt
import com.ioannuwu.inline.domain.wrapper.RangeHighlighterWrapper
import com.ioannuwu.inline.utils.TEST
import com.ioannuwu.inline.utils.TestRangeHighlighterWrapper
import org.junit.Test
import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics

class RenderElementsProviderTest {

    @Test
    fun `render elements provider provides correct elements`() {

        val provider = RenderElementsProvider.Impl(TestRenderDataProvider, TestFontData, TestFontData, TestNumberOfWhitespaces)

        val mostImportantHelper = TestHighlighterHelper(300, 300)

        val highlighters = listOf( mostImportantHelper, TestHighlighterHelper(100, 100), TestHighlighterHelper(10, 10))

        val renderElements = provider.provide(10, highlighters).entries
            .sortedBy { -it.key.priority }
            .map { it.value }

        highlighters.forEach { println(it) }
        renderElements.forEach { println(it) }

        assert(renderElements[0].size == 2)
        assert(renderElements[1].size == 1)
        assert(renderElements[2].size == 1)

        assert(renderElements[0].filterIsInstance<RenderElementKt.Background>().size == 1)
        assert(renderElements[1].filterIsInstance<RenderElementKt.Background>().isEmpty())
        assert(renderElements[2].filterIsInstance<RenderElementKt.Background>().isEmpty())
    }
}

private class TestHighlighterHelper(offset: Int, priority: Int) : TestRangeHighlighterWrapper(
    priority, "test", offset, 10, true, true
) {
    override fun toString(): String = "Helper { $offset }"
}

private object TestRenderDataProvider : RenderDataProvider {

    override fun provide(highlighter: RangeHighlighterWrapper): RenderData = RenderData( // only background
        true, false, true, false, Color.RED, Color.BLACK, Color.WHITE, 2, 3,
        EffectType.BOX, "test", DefaultSettings.Icons.OTHER_ERROR, TextStyle.RUST_STYLE_UNDER_LINE, false
    )

    override fun isValid(highlighter: RangeHighlighterWrapper): Boolean = TEST()
}

private object TestFontData : FontData {
    override val font: Font
        get() = TEST()
    override val fontMetrics: FontMetrics
        get() = TEST()
    override val lineHeight: Int
        get() = TEST()
}

private object TestNumberOfWhitespaces : NumberOfWhitespaces {
    override val numberOfWhitespaces: Int
        get() = 2
}