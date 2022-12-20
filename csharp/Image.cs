Response.Clear();
Response.ContentType = "image/jpeg";
Bitmap image = new Bitmap(500, 200);
Graphics g = Graphics.FromImage(image);
g.InterpolationMode = InterpolationMode.High;//设置高质量插值法
g.SmoothingMode = SmoothingMode.HighQuality;//设置高质量，低速度呈现平滑程度
g.Clear(Color.White);
EncoderParameters param = new EncoderParameters(1);
param.Param[0] = new EncoderParameter(Encoder.Quality, 100L);//100L最高质量
image.Save(Response.OutputStream, ImageCodecInfo.GetImageEncoders()[1], param);
Response.End();