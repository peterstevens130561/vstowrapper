using System;
using System.Collections.Generic;
using System.Dynamic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;
using Microsoft.VisualStudio.Coverage.Analysis;

namespace CodeCoverage.Model
{
    public class SequencePoint
    {
        [XmlAttribute] public uint vc;
        [XmlAttribute] public uint uspid;
        [XmlAttribute] public uint ordinal;
        // ofset from method start in cli
        [XmlAttribute] public uint offset;

        //start line
        [XmlAttribute] public uint sl;
        
        //start columns
        [XmlAttribute] public uint sc;
        
        //emd line[XmlAtrribute]
        [XmlAttribute] public uint el;

        //end column
        [XmlAttribute] public uint ec;
        private Summary parentSummary;

        protected SequencePoint() : this(null)
        {
            
        }
        protected  SequencePoint(Summary summary)
        {
            parentSummary = summary;
        }

        public static SequencePoint Create(Method method)
        {
            Summary summary = method.Summary;
            return new SequencePoint(summary);
        }


        /// <summary>
        /// Load column, line and coverage info from blockLineRange
        /// </summary>
        /// <param name="blockLineRange"></param>
        public void LoadBlockLineRange(BlockLineRange blockLineRange)
        {
            ec = blockLineRange.EndColumn;
            sc = blockLineRange.StartColumn;
            el = blockLineRange.EndLine;
            sl = blockLineRange.StartLine;
        }
    }
}
