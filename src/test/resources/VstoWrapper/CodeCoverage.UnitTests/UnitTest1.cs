using System;
using System.IO;
using System.Xml;
using System.Xml.Serialization;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using CodeCoverage.Model;

namespace CodeCoverage.UnitTests
{
    [TestClass]
    public class UnitTest1
    {
        private XmlSerializer serializer;
        private TextWriter textWriter;
        private XmlWriter writer;
        [TestInitialize]
        public void TestInitialize()
        {
            //serializer = new XmlSerializer(typeof(CoverageSession));
            textWriter = new StringWriter();
            writer = new XmlTextWriter(textWriter);
        }
        [TestMethod]
        public void TestMethod1()
        {

            var coverageSession = new CoverageSession();
            serializer.Serialize(writer,coverageSession);
            string result = textWriter.ToString();
            var expected = "<<?xml version=\"1.0\" encoding=\"utf-16\"?><CoverageSession xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" />>";
            Assert.AreEqual(expected,result);
        }

        [TestMethod]
        public void OneEmptyModule()
        {

            var coverageSession = new CoverageSession();
            coverageSession.Modules.Add(new Module());
            serializer.Serialize(writer, coverageSession);
            string result = textWriter.ToString();
            var expected =
                "<?xml version=\"1.0\" encoding=\"utf-16\"?><CoverageSession xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><Modules><Module skippedDueTo=\"Filter\" /></Modules></CoverageSession>";
            Assert.AreEqual(expected, result);
        }

        [TestMethod]
        public void EmptySummary()
        {

            var coverageSession = new CoverageSession();
            serializer = new XmlSerializer(typeof (Summary));
            var summary = new Summary(null);
            serializer.Serialize(writer, summary);
            string result = textWriter.ToString();
            var expected =
                "<Summary xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" numSequencePoints=\"0\" visitedSequencePoints=\"0\" numBranchPoints=\"0\" visitedBranchPoints=\"0\" sequenceCoverage=\"0\" branchCoverage=\"0\" maxCyclomaticComplexity=\"0\" numCyclomaticComplexity=\"0\" />";
            Assert.AreEqual(expected, result);
        }

        [TestMethod]
        public void EmptySummary2()
        {

            var coverageSession = new CoverageSession();
            serializer = new XmlSerializer(typeof (Summary));
            var summary = new Summary(null);
            serializer.Serialize(writer, summary);
            string result = textWriter.ToString();
            var expected =
                "<Summary xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" numSequencePoints=\"0\" visitedSequencePoints=\"0\" nuumBranchPoints=\"0\" visitedBranchPoints=\"0\" sequenceCoverage=\"0\" branchCoverage=\"0\" maxCyclomaticComplexity=\"0\" numCyclomaticComplexity=\"0\" />";
            Assert.AreEqual(expected, result);
        }

        [TestMethod]
        public void NestedSummary()
        {
            serializer = new XmlSerializer(typeof (Methods));
            var methods =new Methods(null);

            serializer.Serialize(writer, methods);
            string result = textWriter.ToString();
            Assert.AreEqual(null, result);
        }

    }

 
}
