using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace CodeCoverage.Model 
{
 
    public class Module
    {
        public Module()
        {
            skippedDueTo = null;
        }
        [XmlAttribute] 
        public String skippedDueTo;

        [XmlAttribute] 
        public String hash;

        [XmlElement] public String FullName;

        [XmlElement] public String ModuleName;

        public List<Class> Classes;


        /// <summary>
        /// Find the class by the given name, if it does not exist
        /// create a new one and return reference
        /// </summary>
        /// <param name="className"></param>
        /// <returns></returns>
        internal Class FindClassByName(string className)
        {
            foreach (var clazz in Classes)
            {
                if (clazz.FullName == className)
                {
                    return clazz;
                }
            }
            var newClazz = new Class();
            newClazz.FullName = className;
            Classes.Add(newClazz);
            return newClazz;

        }
    }
}
