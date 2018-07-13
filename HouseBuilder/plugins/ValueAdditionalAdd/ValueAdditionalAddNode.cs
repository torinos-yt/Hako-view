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
	[PluginInfo(Name = "AdditionalAdd", Category = "Value", Help = "Basic template with one value in/out", Tags = "")]
	#endregion PluginInfo
	public class ValueAdditionalAddNode : IPluginEvaluate
	{
		#region fields & pins
		[Input("Input", DefaultValue = 1.0)]
		public ISpread<double> FInput;

		[Output("Output")]
		public ISpread<double> FOutput;

		[Import()]
		public ILogger FLogger;
		#endregion fields & pins

		//called when data for any output pin is requested
		public void Evaluate(int SpreadMax)
		{
			FOutput.SliceCount = SpreadMax;
			double tmp = 0;

			for (int i = 0; i < SpreadMax; i++){
				FOutput[i] = FInput[i] + tmp;
				tmp = FOutput[i];
			}
				

			//FLogger.Log(LogType.Debug, "hi tty!");
		}
	}
}
