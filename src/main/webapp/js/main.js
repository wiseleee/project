(function($) {

	"use strict";

	var fullHeight = function() {

		$('.js-fullheight').css('height', $(window).height());
		$(window).resize(function(){
			$('.js-fullheight').css('height', $(window).height());
		});

	};
	fullHeight();

	$('#sidebarCollapse').on('click', function () {
      $('#sidebar').toggleClass('active'); //설정된 클래스명을 해당하는 요소가 가지고 있는지 판단하여 해당 요소가 있으면 이를 제거,해당 요소가 없다면 이를 부여
  });
     
})(jQuery);