#region usings
using System;
using System.ComponentModel.Composition;

using VVVV.PluginInterfaces.V1;
using VVVV.PluginInterfaces.V2;
using VVVV.Utils.VColor;
using VVVV.Utils.VMath;

using VVVV.Core.Logging;
#endregion usings

namespace VVVV.Nodes
{
	#region PluginInfo
	[PluginInfo(Name = "add_value", Category = "Value", Help = "Basic template with one value in/out", Tags = "")]
	#endregion PluginInfo
	public class Valueadd_valueNode : IPluginEvaluate
	{
		
		#region fields & pins
		[Input("Input", DefaultValue = 1.0)]
		public double FInput;

		[Output("Output")]
		public double FOutput;
		
		[Output("Debbug")]
		public double Debbug;

		[Import()]
		public ILogger FLogger;
		#endregion fields & pins

		//called when data for any output pin is requested
		public void Evaluate(int SpreadMax)
		{
			Debbug = 0;
			
			FOutput = 0;

			

			//FLogger.Log(LogType.Debug, "hi tty!");
		}
	}
}
