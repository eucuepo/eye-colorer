<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Image Upload</title>
<script type="text/javascript" src="script/jquery.js"></script>
<script type="text/javascript" src="script/jscolor.js"></script>
<script type="text/javascript" src="script/fileuploader.js" />
<script type="text/javascript">
	//prepare the form when the DOM is ready 
	$(document).ready(function() {

		var thumb = $('#thumb');

		new AjaxUpload('imageUpload', {
			action : $('#newHotnessForm').attr('action'),
			name : 'image',
			onSubmit : function(file, extension) {
				$('#preview').addClass('loading');
			},
			onComplete : function(file, response) {
				thumb.load(function() {
					$('#preview').removeClass('loading');
					thumb.unbind();
				});
				thumb.attr('src', response);
			}
		});
	});
</script>
</head>

<body>
	<form action="UploadImage" method="post" enctype="multipart/form-data" id="newHotnessForm">
		<table width="400px" align="center" border=0 style="background-color: ffeeff;">
			<tr>
				<td align="center" colspan=2 style="font-weight: bold; font-size: 20pt;">Image Details</td>
			</tr>

			<tr>
				<td align="center" colspan=2>&nbsp;</td>
			</tr>

			<tr>
				<td>Image Link:</td>
				<td><input type="file" size="20" id="imageUpload" class=" " name="image"/></td>
				<td><input class="color" name="color" /></td>
				<td><button class="button" type="submit">Save</button></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
		</table>
	</form>
	<div id="upload-area">
		<div id="preview">
			<img width="100px" height="100px" src="script/hs.png" id="thumb" />
		</div>

	</div>
</body>

</html>