using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace CodeCoverage.Model
{
    public class Method
    {
        [XmlAttribute] public Boolean visited;
        [XmlAttribute] public int cyclomaticComplexity, sequenceCoverage, branchCoverage;
        [XmlAttribute] public Boolean isConstructor, isStatic, isGetter, isSetter;
        public Summary Summary;
        public int MetaDataToken;
        public string Name;
        public FileRef FileRef;
        public List<BranchPoint> BranchPoints;
        public List<SequencePoint> SequencePoints;

        protected Method()
        {
            
        }
        protected Method(Summary parentSummary)
        {
            Summary = new Summary(parentSummary);
            BranchPoints = new List<BranchPoint>();
            SequencePoints = new List<SequencePoint>();
        }

        public static Method Create(Class clazz)
        {
            Summary parentSummary = clazz != null ? clazz.Summary : null;
            Method method = new Method(parentSummary);
            return method;
        }
    }
}
