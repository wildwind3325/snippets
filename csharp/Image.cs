Response.Clear();
Response.ContentType = "image/jpeg";
Bitmap image = new Bitmap(500, 200);
Graphics g = Graphics.FromImage(image);
g.InterpolationMode = InterpolationMode.High;//���ø�������ֵ��
g.SmoothingMode = SmoothingMode.HighQuality;//���ø����������ٶȳ���ƽ���̶�
g.Clear(Color.White);
EncoderParameters param = new EncoderParameters(1);
param.Param[0] = new EncoderParameter(Encoder.Quality, 100L);//100L�������
image.Save(Response.OutputStream, ImageCodecInfo.GetImageEncoders()[1], param);
Response.End();