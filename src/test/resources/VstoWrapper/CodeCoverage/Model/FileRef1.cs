using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace CodeCoverage.Model
{
    public class FileRef
    {
        [XmlAttribute]
        public string uid;
    }
}
