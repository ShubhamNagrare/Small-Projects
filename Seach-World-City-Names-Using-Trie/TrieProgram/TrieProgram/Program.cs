using ExcelDataReader;
using IronXL;
using OfficeOpenXml;
using System;
using System.Collections.Generic;
using System.Data;
using System.Diagnostics;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;

namespace TrieProgram
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Trie Program Started !!!!");
            Console.WriteLine();Console.WriteLine();
            List<string> cityListFromExcell = new Util().GetWorldCityList();  // Total 26570

            Trie trie = new Trie();
            trie.InsertRange(cityListFromExcell);

            Console.WriteLine(); Console.WriteLine();
            Console.WriteLine("ENTER CITY NAME TO SEARCH : =====>");
            var userInput = Console.ReadLine(); Console.WriteLine();

            var stopwatch = new Stopwatch(); stopwatch.Start();
            Console.WriteLine("SEARCH USING FOR LOOP STARTED....!!!!");
            for (int i = 0; i < cityListFromExcell.Count; i++)
            {
                if (userInput == cityListFromExcell[i])
                {
                    stopwatch.Stop();
                    Console.WriteLine("City Found via Loop : " + userInput + "   in Time " + stopwatch.ElapsedTicks); break;
                }
            }
            Console.WriteLine();Console.WriteLine();

            stopwatch.Reset();stopwatch.Start();
            Console.WriteLine("SEARCH USING FOR TRIE STARTED....!!!!");
            var prefix = trie.Prefix(userInput);
            var isExist = prefix.Depth == userInput.Length && prefix.FindChildrenTrieNode('$') != null;
            if (isExist)
            {
                stopwatch.Stop();
                Console.WriteLine("City Found via Trie : " + userInput + "   in Time " + stopwatch.ElapsedTicks);
            }
            Console.ReadKey();

        }
    }

    public class Util
    {
        public List<string> GetWorldCityList()
        {
            var file = "C:\\Users\\91973\\Desktop\\10901\\worldcities.xlsx";
            List<string> cityList = new List<string>();
            System.Text.Encoding.RegisterProvider(System.Text.CodePagesEncodingProvider.Instance);
            using (var stream = File.Open(file, FileMode.Open, FileAccess.Read))
            {
                using (var reader = ExcelReaderFactory.CreateReader(stream))
                {
                    // int c = 0;
                    do
                    {
                        while (reader.Read()) //Each ROW
                        {
                            // c++;
                            for (int column = 0; column < 1; column++)
                            {
                                // if (c == 100)
                                // {
                                //     return cityList;
                                // }
                                var word = RemoveSpecialCharacters(reader.GetValue(column).ToString().ToLower());
                                Console.WriteLine(word);
                                cityList.Add(word);
                                // if (c==1)
                                // {
                                //     cityList.Add("colgate");
                                //     Console.WriteLine("colgate");
                                // }
                            }
                        }
                    } while (reader.NextResult()); //Move to NEXT SHEET
                }
            }
            return cityList;
        }

        public string RemoveSpecialCharacters(string str)
        {
            return Regex.Replace(str, "[^a-z]+", "", RegexOptions.Compiled);
        }
    }




    public class Trie
    {
        private TrieNode root;

        public Trie()
        {
            root = new TrieNode('^', 0, null);
        }


        public TrieNode Prefix(string s)
        {
            var currentNode = root;
            var result = currentNode;

            foreach (var c in s)
            {
                currentNode = currentNode.FindChildrenTrieNode(c);
                if (currentNode == null)
                    break;
                result = currentNode;
            }
            return result;
        }

        public bool Search(string s)
        {
            var prefix = Prefix(s);
            return prefix.Depth == s.Length && prefix.FindChildrenTrieNode('$') != null;
        }
        public void InsertRange(List<string> items)
        {
            for (int i = 0; i < items.Count; i++)
                Insert(items[i]);
        }

        public void Insert(string s)
        {
            var commonPrefix = Prefix(s);
            var current = commonPrefix;

            for (var i = current.Depth; i < s.Length; i++)
            {
                var newNode = new TrieNode(s[i], current.Depth + 1, current);
                current.Children.Add(newNode);
                current = newNode;
            }

            current.Children.Add(new TrieNode('$', current.Depth + 1, current));
        }

    }


    public class TrieNode
    {
        public char Value { get; set; }
        public List<TrieNode> Children { get; set; }
        public TrieNode Parent { get; set; }
        public int Depth { get; set; }

        public TrieNode(char value, int depth, TrieNode parent)
        {
            Value = value;
            Depth = depth;
            Parent = parent;
            Children = new List<TrieNode>();
        }

        public bool IsLeaf()
        {
            return Children.Count == 0;
        }

        public TrieNode FindChildrenTrieNode(char c)
        {
            foreach (var child in Children)
            {
                if (child.Value == c)
                {
                    return child;
                }
            }
            return null;
        }
    }
}


