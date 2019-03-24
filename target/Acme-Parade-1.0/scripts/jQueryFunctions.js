function checkPhone(message) {
	var phoneNumber = document.getElementById("actor.phoneNumber");

	if (phoneNumber.value != "") {
		var expreg = /(^[+][1-9]\d{0,2}[ ][(][1-9]\d{0,2}[)][ ]\d{4,}$)|(^[+][1-9]\d{0,2}[ ]\d{4,}$)|(^\d{4,}$)/;
		if (!expreg.test(phoneNumber.value)) { return checkPosting(message); }
	}
};

function checkPosting(message) {
	var con = confirm(message);
	if (con) {
		return true;
	} else {
		return false;
	}
};