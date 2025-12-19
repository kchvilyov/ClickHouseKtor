import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TextSpliteratorTest {
    @Test
    fun nextChunkFlush() {
        val spliterator = TextSpliterator()
        spliterator.nextChunk("ab")
        spliterator.nextChunk("cd\nef")
        var res = spliterator.flush()
        assertEquals(1, res.size)
        assertEquals( listOf("abcd"), res )
        spliterator.nextChunk("gh")
        spliterator.nextChunk("\nxyz")
        spliterator.nextChunk("\n")
        res = spliterator.flush()
        assertEquals(2, res.size)
        assertEquals(listOf("efgh", "xyz"), res )

    }
}