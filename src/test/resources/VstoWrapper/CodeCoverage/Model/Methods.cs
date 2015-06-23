using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeCoverage.Model
{
    public class Methods
    {

        public List<Method> methods;

        public Summary Summary;
        public Methods(Summary parent)
        {
            Summary = new Summary(parent);
        }

        public Methods()
        {
            
        }
    }
}
