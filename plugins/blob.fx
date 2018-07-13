
RWByteAddressBuffer Outbuf : BACKBUFFER;
Texture2D<float4> tex;

int Length;
int Width;
int Height;


#if !defined(XTHREADS)
#define XTHREADS 1
#endif
#if !defined(YTHREADS)
#define YTHREADS 1
#endif
#if !defined(ZTHREADS)
#define ZTHREADS 1
#endif

struct csin
{
	uint3 DTID : SV_DispatchThreadID;
	uint3 GTID : SV_GroupThreadID;
	uint3 GID : SV_GroupID;
};

[numthreads(XTHREADS, YTHREADS, ZTHREADS)]
void CS(csin input)
{
	float result = 0;
	//if(input.DTID.x > somecount) return;
	 for (int i = Height; i <= Height; i++)
    {
        for (int j = Width; j <= Width; j++)
        {
            int u = (input.DTID.x * Width) + Width + j;
            int v = (input.DTID.y * Height) + Height + i;
            float4 tex = tex[int2(u,v)];
            result += tex.a;
        }
    }
	//Outbuf.Store3( 0 , asuint(0.1) );
}
technique11 cst { pass P0{SetComputeShader( CompileShader( cs_5_0, CS() ) );} }