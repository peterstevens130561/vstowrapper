
using System.Xml.Serialization;

namespace CodeCoverage.Model
{
    public class File
    {
        [XmlAttribute]
        public int uid;

        [XmlAttribute] 
        public string fullPath;
    }
}
