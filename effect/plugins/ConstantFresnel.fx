//@author: vux
//@help: template for standard shaders
//@tags: template
//@credits: 

Texture2D texture2d <string uiname="Texture";>;

SamplerState linearSampler : IMMUTABLE
{
    Filter = MIN_MAG_MIP_LINEAR;
    AddressU = Clamp;
    AddressV = Clamp;
};
 
cbuffer cbPerDraw : register( b0 )
{
	float4x4 tVP : LAYERVIEWPROJECTION;	
	float4x4 tWV : LAYERWORLDPROJECTION;
	float4x4 tWVP : LAYERWORLDVIEWPROJECTION;
	float3 CamPos;
	float3 CPos;
};

cbuffer cbPerObj : register( b1 )
{
	float camdistance;
	float radius;
	float4x4 tW : WORLD;
	float4 cAmb <bool color=true;String uiname="Color";> = { 1.0f,1.0f,1.0f,1.0f };
};

struct VS_IN
{
	float4 PosO : POSITION;
	float4 TexCd : TEXCOORD0;
	float3 Norm : NORMAL;

};

struct vs2ps
{
    float4 PosWVP: SV_Position;
    float4 TexCd: TEXCOORD0;
	float3 Norm : NORMAL;
	float alpha : TEXCOORD2;
	float3 Eye : TEXCOORD1;
};

vs2ps VS(VS_IN input)
{
	
    vs2ps output;
	float d = distance(mul(input.PosO, tW).xyz, CPos);
	output.alpha = 1 - (d / radius);
    output.PosWVP  = mul(input.PosO,mul(tW, tVP));
	float4 EyeNorm = mul(input.PosO, tWVP);
    output.TexCd = input.TexCd;
	output.Eye = normalize(EyeNorm - CamPos);
	output.Norm = input.Norm;
    return output;
}




float4 PS(vs2ps In): SV_Target
{
	//float a = (camdistance < radius + .15)? .9 :pow(abs(dot(In.Eye, In.Norm)) ,1.5);
	float4 col =(texture2d.Sample(linearSampler,In.TexCd.xy) * cAmb ) ;
	col.a *= pow(abs(In.alpha), 1.2);
    return col;
}





technique10 Constant
{
	pass P0
	{
		SetVertexShader( CompileShader( vs_4_0, VS() ) );
		SetPixelShader( CompileShader( ps_4_0, PS() ) );
	}
}




