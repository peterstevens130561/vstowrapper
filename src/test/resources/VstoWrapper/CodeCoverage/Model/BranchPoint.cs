using System.ComponentModel;
using System.Xml.Serialization;

namespace CodeCoverage.Model
{
    public class BranchPoint
    {
        private Summary parentSummary;
        [XmlAttribute] public int vc,
            uspid,
            oridinal,
            offset,
            path;

        protected BranchPoint()
        {
            
        }

        public BranchPoint(Summary summary)
        {
            parentSummary = summary;
        }
    }
}
