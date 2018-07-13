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
	[PluginInfo(Name = "MargeArea", Category = "Value", Help = "Basic template with one value in/out", Tags = "")]
	#endregion PluginInfo
	public class ValueMargeAreaNode : IPluginEvaluate
	{
		#region fields & pins
		[Input("Input", DefaultValue = 1.0)]
		public ISpread<double> FInput;
		
		[Input("Target Position", DefaultValue = 0.0)]
		public Vector2D mPos;
		
		[Input("Input2", DefaultValue = 1.0)]
		public ISpread<double> FInoput;
		

		[Output("Output")]
		public ISpread<double> FOutput;
		
		[Output("Out")]
		public Vector2D FOtput;

		[Import()]
		public ILogger FLogger;
		#endregion fields & pins

		//called when data for any output pin is requested
		public void Evaluate(int SpreadMax)
		{
			FOutput.SliceCount = SpreadMax;
			Vector2D a = mPos * 2;
			for (int i = 0; i < SpreadMax; i++)
				FOutput[i] = FInput[i] * 3;
			
			FOtput = a;

			//FLogger.Log(LogType.Debug, "hi tty!");
		}
	}
}
