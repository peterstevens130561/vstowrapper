using System;
using System.CodeDom;
using System.IO;
using System.Runtime.Remoting.Messaging;
using System.Xml;
using System.Xml.Serialization;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using CodeCoverage.Model;

namespace CodeCoverage.UnitTests
{
    [TestClass]
    public class SequencePointsTest
    {

        const string HEADER = "<?xml version=\"1.0\" encoding=\"utf-16\"?>";
        const string METHODFIRST = "<Method xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" visited=\"false\" cyclomaticComplexity=\"0\" sequenceCoverage=\"0\" branchCoverage=\"0\" isConstructor=\"false\" isStatic=\"false\" isGetter=\"false\" isSetter=\"false\">" ;
        private const string METHODCLOSE = "<Method />";
        private const string SUMMARY =
            "<Summary numSequencePoints=\"0\" visitedSequencePoints=\"0\" numBranchPoints=\"0\" visitedBranchPoints=\"0\" sequenceCoverage=\"0\" branchCoverage=\"0\" maxCyclomaticComplexity=\"0\" numCyclomaticComplexity=\"0\" />";

        private string METADATATOKEN = "<MetaDataToken>0</MetaDataToken>";
        private const string EMPTYBRANCHPOINTS = "<BranchPoints />";
        private const string EMPTYSEQUENCEPOINTS = "<SequencePoints />";
        private XmlSerializer serializer;
        private TextWriter textWriter;
        private XmlWriter writer;

        void TestInitialize()
        {
            //serializer = new XmlSerializer(typeof(CoverageSession));
            textWriter = new StringWriter();
            writer = new XmlTextWriter(textWriter);
        }
        [TestMethod]
        public void SimpleListTest()
        {
            serializer = new XmlSerializer(typeof (Method));
            var method = Method.Create(null);
            var sequencePoint = SequencePoint.Create(method);
            serializer.Serialize(writer, method);
            string result = textWriter.ToString();
            string expected = HEADER + 
                METHODFIRST + SUMMARY + METADATATOKEN + EMPTYBRANCHPOINTS +
            EMPTYSEQUENCEPOINTS + METHODCLOSE;

            Assert.AreEqual(expected, result);

        }

        [TestMethod]
        public void SimpleListTest2()
        {
            serializer = new XmlSerializer(typeof(Method));
            var method = Method.Create(null);
            var sequencePoint = SequencePoint.Create(method);

            serializer.Serialize(writer, method);
            string result = textWriter.ToString();
            string expected = "<?xml version=\"1.0\" encoding=\"utf-16\"?>" +
                "<Method xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" visited=\"false\" cyclomaticComplexity=\"0\" sequenceCoverage=\"0\" branchCoverage=\"0\" isConstructor=\"false\" isStatic=\"false\" isGetter=\"false\" isSetter=\"false\">" +
            "<Summary numSequencePoints=\"0\" visitedSequencePoints=\"0\" numBranchPoints=\"0\" visitedBranchPoints=\"0\" sequenceCoverage=\"0\" branchCoverage=\"0\" maxCyclomaticComplexity=\"0\" numCyclomaticComplexity=\"0\" />" +
            "<MetaDataToken>0</MetaDataToken>" +
            "<BranchPoints /><SequencePoints /></Method>";
            Assert.AreEqual(expected, result);

        }
    }
}
