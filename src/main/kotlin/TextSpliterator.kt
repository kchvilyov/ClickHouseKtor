import java.util.LinkedList

class TextSpliterator {
    var repo = LinkedList<StringBuilder>()

    fun nextChunk(chunk: String) {
        val parts = chunk.split("\n")
        if (parts.isEmpty()) return
        var firstPart = true
        for (part in parts) {
            if (firstPart) {
                if (repo.isEmpty())
                    repo.add(StringBuilder(part))
                else
                    repo.last.append(part)
                firstPart = false
            } else repo.add(StringBuilder(part))
        }
    }

    fun flush(): List<String> {
        val last = repo.removeLast()
        val res = repo
        repo = LinkedList()
        repo.add(last)
        return res.map { it.toString() }
    }
}