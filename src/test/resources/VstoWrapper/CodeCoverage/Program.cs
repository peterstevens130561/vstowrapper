using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using CodeCoverage.Model;
using Microsoft.VisualStudio.Coverage.Analysis;

// http://blogs.msdn.com/b/phuene/archive/2009/12/01/programmatic-coverage-analysis-in-visual-studio-2010.aspx

namespace CodeCoverage
{
    internal class Program
    {
        private static void Main(string[] args)
        {
            using (CoverageInfo info = CoverageInfo.CreateFromFile(args[0]))
            {
                CoverageDS dataSet = info.BuildDataSet();
                dataSet.ExportXml(args[1]);
            }
        }

        private void Fun()
        {
            using (CoverageInfo info = CoverageInfo.CreateFromFile("foo.coverage"))
            {
                CoverageDS dataSet = info.BuildDataSet();
                dataSet.ExportXml("hi");
                List<BlockLineRange> lines = new List<BlockLineRange>();
                CoverageSession coverageSesson = new CoverageSession();

                foreach (ICoverageModule coverageModule in info.Modules)
                {
                    Module modelModule = new Module();
                    coverageSesson.Modules.Add(modelModule);
                    modelModule.FullName = coverageModule.Name;

                    byte[] coverageBuffer = coverageModule.GetCoverageBuffer(null);
                    using (ISymbolReader reader = coverageModule.Symbols.CreateReader())
                    {
                        uint methodId;
                        string methodName;
                        string undecoratedMethodName;
                        string className;
                        string namespaceName;

                        lines.Clear();
                        while (reader.GetNextMethod(out methodId, out methodName, out undecoratedMethodName, out className, out namespaceName, lines))
                        {
                            DumpLines(null,lines);
                            var modelClass=modelModule.FindClassByName(className);
                            var modelMethod = Method.Create(null);
                            modelClass.Methods.Add(modelMethod);

                            

                            CoverageStatistics stats = CoverageInfo.GetMethodStatistics(coverageBuffer, lines);

                            Console.WriteLine("Method {0}{1}{2}{3}{4} has:",
                                namespaceName == null ? "" : namespaceName,
                                string.IsNullOrEmpty(namespaceName) ? "" : ".",
                                className == null ? "" : className,
                                string.IsNullOrEmpty(className) ? "" : ".",
                                methodName
                                );

                            Console.WriteLine("    {0} blocks covered", stats.BlocksCovered);
                            Console.WriteLine("    {0} blocks not covered", stats.BlocksNotCovered);
                            Console.WriteLine("    {0} lines covered", stats.LinesCovered);
                            Console.WriteLine("    {0} lines partially covered", stats.LinesPartiallyCovered);
                            Console.WriteLine("    {0} lines not covered", stats.LinesNotCovered);
                            lines.Clear();
                        }
                    }
                }
            }
        }

        private void DumpLines(Method method,List<BlockLineRange> lines)
        {
            foreach (var blockLineRange in lines)
            {
                SequencePoint point = SequencePoint.Create(method);
                point.LoadBlockLineRange(blockLineRange);
            }
        }
    }
}
  
