function Grid (raw) {
	//this.cells;
	
	/**
	 * 
	 */
	this.fill = function (raw) {
		raw = raw.replace(/[^0-9 ]/g, "");
		
		if (raw.length != 81)
			return false;
		
		this.cells = new Array();
		
		var r,c;
		for (r=0; r<9; r++) {
			this.cells[r] = new Array();
			
			for (c=0; c<9; c++) {
				this.cells[r][c] = raw.charAt(r*9-(-c));
			}
		}
		return true;
	};
	
	if (!this.fill(raw))
		return null;
	
	
	/**
	 * 
	 */
	this.toHTML = function () {
		var code = "<table class=\"grid\">\n";
		
		var r,c;
		for (r=0; r<9; r++) {
			code += "\t<tr>";
			
			for (c=0; c<9; c++) {
				code += " <td>"+this.cells[r][c]+"</td>";
			}
			
			code += " </tr>\n";
		}
		
		code += "</table>\n";
		
		return code;
	};
}