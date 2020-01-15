String.prototype.endsWith = function(str) {
  return (this.match(str+"$")==str)
}

function getUrlParameter(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) 
        {
            return sParameterName[1];
        }
    }
}      

	$(document).ready(function() {
		$('a.switchTreeState').click(function() {
			var close = 2;//2 - Do nothing
			var target = "";
			var length = "but_".length;
			$(this).children("img").each(
				function(index) {
					var currentImg = $(this).attr("src");
					if (currentImg.endsWith("plus.png") || currentImg.endsWith("minus.png")) {
						target = $(this).attr("name").substr(length);
						if (currentImg.endsWith("plus.png")) {
							$(this).attr('src','img/minus.png');
						  close = 0;//0 - open the tree
						} else {
						  $(this).attr('src','img/plus.png');
						  close = 1; //1 - close the tree
						}
					}
				}
			);
			var targetid = "ul#" + target;
			
			if (close == 1) {
				$(targetid).toggle();
				
			} else if (close == 0) {				
				$(targetid).toggle();				
			}
		});
		
		//On ferme tout
		$("ul#browser").find("ul").hide();	
		//$("ul#browser li.section a img").each(
		$("ul#browser").find("img").each(
			function() {
				var currentImg = $(this).attr("src");
				if (currentImg.endsWith("plus.png") || currentImg.endsWith("minus.png")) {
					$(this).attr('src','img/plus.png');		
				}
			}
		);
		
		var currentpage = getUrlParameter("page");
		if (currentpage.length > 0) {
			$('a').each(
				function() {
					var currentLink = $(this).attr("href");
					if (typeof currentLink !== "undefined" && currentLink.length > 0) {
						var linkArray = currentLink.split('.');
						var filename = linkArray[0];
						if (filename.length > 0 && filename === currentpage) {
							$(this).closest("li").css("background-color","#d7eafd");
							$(this).parents("ul").each(
								function() {									
									$(this).show();
									$(this).parent("ul").children("li").first().find("img").each(
										function() {
											var currentImg = $(this).attr("src");
											if (currentImg.endsWith("plus.png") || currentImg.endsWith("minus.png")) {
												$(this).attr('src','img/minus.png');		
											}
										}
									);
								}
							);
							return false;
						}
					}
				}
			);
		}
	});
