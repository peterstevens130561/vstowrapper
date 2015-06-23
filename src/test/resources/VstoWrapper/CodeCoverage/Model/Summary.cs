using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace CodeCoverage.Model
{
    public class Summary
    {
        private Summary parent;
        [XmlAttribute] public int numSequencePoints,
            visitedSequencePoints,
            numBranchPoints,
            visitedBranchPoints,
            sequenceCoverage,
            branchCoverage,
            maxCyclomaticComplexity,
            numCyclomaticComplexity;

        public Summary()
        {
            
        }

        public Summary(Summary parent)
        {
            this.parent = parent;
        }

        public void SequencePointVisited()
        {
            numSequencePoints += 1;
            visitedSequencePoints += 1;
        }
    }
}
