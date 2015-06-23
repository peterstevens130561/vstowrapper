using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeCoverage.Model
{
    public class Class
    {
        public Summary Summary;
        public string FullName;
        public List<Method> Methods;
    }
}
