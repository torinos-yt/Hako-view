#region usings
using System;
using System.ComponentModel.Composition;
using System.Collections.Generic;

using VVVV.PluginInterfaces.V1;
using VVVV.PluginInterfaces.V2;
using VVVV.Utils.VColor;
using VVVV.Utils.VMath;

using VVVV.Core.Logging;
#endregion usings

namespace VVVV.Nodes
{
	#region PluginInfo
	[PluginInfo(Name = "AreaMarge", Category = "Value", Help = "Basic template with one value in/out", Tags = "")]
	#endregion PluginInfo
	public class ValueAreaMargeNode : IPluginEvaluate
	{
		#region fields & pins
		
		[Input("Target Position", DefaultValue = 0.0)]
		public ISpread<Vector3D> tPos;
		
		[Input("Radius", DefaultValue = 0.0)]
		public ISpread<double> rad;
		
		[Input("Insert", DefaultValue = 0.0, IsSingle = true)]
		public bool Ins;
		
		[Input("Reset", DefaultValue = 0.0, IsSingle = true)]
		public bool res;

		[Output("Position")]
		public ISpread<Vector3D> outpos;
		
		[Output("Radius")]
		public ISpread<double> outrud;
		

		[Import()]
		public ILogger FLogger;
		#endregion fields & pins
		
		

		//called when data for any output pin is requested
		public void Evaluate(int SpreadMax)
		{
			

			for (int i = 0; i < SpreadMax; i++){
				outpos[i] = new Vector3D(1.0,1.0,1.0);
				outrud[i] = 2;	
			}
			//FLogger.Log(LogType.Debug, "hi tty!");
		}
	}
}
