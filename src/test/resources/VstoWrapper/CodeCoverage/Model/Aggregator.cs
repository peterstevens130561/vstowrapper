using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeCoverage.Model
{
    class Aggregator
    {
        public Aggregator Parent;

        public Aggregator(Aggregator parent)
        {
            Parent = parent;
        }
    }
}
