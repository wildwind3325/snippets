// 使用FileSystemObject组件，需要引入Microsoft Scripting Runtime

Dim fso As New FileSystemObject
Dim ts As TextStream
Set ts = fso.OpenTextFile(ActiveWorkbook.Path & "\" & "output.txt", ForReading)
Dim line As String
Do While ts.AtEndOfStream <> True
line = ts.ReadLine // 读取一行
Loop
ts.Close

// 不使用任何组件直接写文本文件

Dim DestFile As String
Dim FileNum As Integer
DestFile = InputBox("请填写保存文件名称：", "提示", "E:\1.txt")
If DestFile = "" Then
//未选择
End If
FileNum = FreeFile()
Open DestFile For Output As #FileNum
Print #FileNum, "Sampler Text" & Chr(13) & Chr(10)//写入内容
Close #FileNum