package interviewTask;

import com.sun.source.tree.Tree;
import org.junit.jupiter.api.Test;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Task10AutocompleteWithTrie {

    @Test
    void name() {
        TrieNode.Trie t = new TrieNode.Trie();


        String[] strings = new String[]{"dog", "deer", "deal"};
        Arrays.stream(strings)
                .forEach(d -> t.insert(d));

        List<String> actual = t.autoComplete("de");
        assertEquals(
                List.of("deer", "deal"),
                actual
               );
    }

    private String[] getAr(String[] strings, String prefix) {

        return new String[]{};
    }

    private static class TrieNode {
        char data;
        LinkedList<TrieNode> children;
        TrieNode parent;
        boolean isEnd;

        public TrieNode(char data) {
            this.data = data;
            children = new LinkedList<>();
            isEnd = false;
        }

        public TrieNode getChild(char data) {
            if (children != null) {
                for (TrieNode eachChild : children) {
                    if (eachChild.data == data) {
                        return eachChild;
                    }
                }
            }
            return null;
        }


        protected List<String> getWords() {
            List<String> list = new ArrayList<>();
            if (isEnd) {
                list.add(this.toString());
            }

            if (children != null) {
                for (TrieNode child : children) {
                    if (child != null) {
                        list.addAll(child.getWords());
                    }
                }
            }
            return list;
        }
        public String toString() {
            if (parent == null) {
                return "";
            } else {
                return parent.toString() +
                        new String(new char[] {data});
            }
        }
        static class Trie {
            private TrieNode root;

            public Trie() {
                this.root = new TrieNode(' ');
            }

            public void insert(String word) {
                if(search(word)) return;

                TrieNode current = root;
                TrieNode pre;

                for (char ch : word.toCharArray()) {
                    pre = current;

                    TrieNode child = current.getChild(ch);

                    if (child != null) {
                        current = child;
                        child.parent = pre;
                    } else {
                        current.children.add(new TrieNode(ch));
                        current = current.getChild(ch);
                        current.parent = pre;
                    }
                }
                current.isEnd = true;
            }

            public boolean search(String word) {
                TrieNode current = root;

                for (char ch : word.toCharArray()) {
                    if (current.getChild(ch) == null) {
                        return false;
                    } else {
                        current = current.getChild(ch);
                    }
                }
                return current.isEnd;
            }

            public List<String> autoComplete(String prefix) {
                TrieNode lastNode = root;
                for (int i = 0; i < prefix.length(); i++) {
                    lastNode = lastNode.getChild(prefix.charAt(i));
                }

                if (lastNode == null) {
                    return new ArrayList<>();
                }
                return lastNode.getWords();
            }
        }
    }

}
