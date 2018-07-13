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
	[PluginInfo(Name = "Add_mat", Category = "Value", Help = "Basic template with one value in/out", Tags = "")]
	#endregion PluginInfo
	
	public class ValueAdd_matNode : IPluginEvaluate
	{
		public double Gesture_Value = 0;
		#region fields & pins
		[Input("Input", DefaultValue = 1.0)]
		public ISpread<double> FInput;

		[Output("Output")]
		public ISpread<double> FOutput;
		
		[Output("Debbug")]
		public ISpread<double> De;

		[Import()]
		public ILogger FLogger;
		#endregion fields & pins

		//called when data for any output pin is requested
		public void Evaluate(int SpreadMax)
		{
			FOutput.SliceCount = SpreadMax;
			Gesture_Value = Gesture_Value + FInput[0];
			for (int i = 0; i < SpreadMax; i++)
				//FOutput[i] = Gesture_Value;

			De[0] = 0.0;
			//FLogger.Log(LogType.Debug, "hi tty!");
		}
	}
}
