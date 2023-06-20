package com.example.t9search.feature_search

import android.util.Log
import com.example.t9search.AppConstants.TAG_TRIE_NODE

/**
 * [Trie search tree](https://www.geeksforgeeks.org/trie-insert-and-search/) with
 */
class TrieNode {
    val children: MutableMap<Char, TrieNode> = mutableMapOf()
    var isWord: Boolean = false
    var word: String = ""
}

class Trie {
    private val root: TrieNode = TrieNode()

    fun checkWord(word: String): Boolean {
        var node = root
        for (char in word) {
            val child = node.children[char] ?: return false
            node = child
        }
        return node.isWord
    }

    fun addWord(word: String) {
        var node = root
        for (char in word) {
            val child = node.children.getOrPut(char) {TrieNode() }
//            node.children[char] = child
            node = child
        }
        node.isWord = true
        node.word = word
        Log.i(TAG_TRIE_NODE, "Adding words, node: $node")
    }

    private fun getNodeByPrefix(prefix: String): TrieNode? {
        var node = root
        for (char in prefix) {
            val child = node.children[char] ?:  return null
            node = child
        }
        return node
    }

    private fun getNodesByPrefixes(prefix: List<String>): List<TrieNode> {
        val startNodes = mutableListOf<TrieNode>()
        for (word in prefix) {
            val node = getNodeByPrefix(word)
            if (node != null) {
                startNodes.add(node)
            }
        }
        return startNodes
    }

    fun searchWordsWithPrefix(prefix: List<String>?, limit: Int): List<String> {
        val results = mutableListOf<String>()
        if (prefix == null || prefix.isEmpty()) {
            searchAllWords(root, results, limit)
        } else {
            val startNodes = getNodesByPrefixes(prefix)
            for ((i, node) in startNodes.withIndex()) {
                searchWordsFromNode(node, prefix[i], results, limit)
                if (results.size >= limit) {
                    return results
                }
            }

        }

        Log.i(TAG_TRIE_NODE, "results empty: ${results.isEmpty()}")
        return if (results.isEmpty()) emptyList() else results

    }

    private fun searchAllWords(
        node: TrieNode,
        results: MutableList<String>,
        limit: Int
    ) {
        if (results.size >= limit) {
            return
        }
        if (node.isWord) {
            results.add(node.word)
        }
        for ((_, child) in node.children) {
            searchAllWords(child, results, limit)
        }
    }

    fun searchWordsFromNode(
        node: TrieNode,
        prefix: String,
        results: MutableList<String>,
        limit: Int
    ) {
        if (results.size >= limit ){
            return
        }
        if (node.isWord){
            results.add(node.word)
            Log.i(TAG_TRIE_NODE, "for results prefix ${node.word}")
        }

        for ((char, child) in node.children) {
            val newPrefix = prefix + char
            searchWordsFromNode(child, newPrefix, results, limit)
        }
    }
}