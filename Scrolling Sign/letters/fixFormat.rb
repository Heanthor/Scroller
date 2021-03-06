letters = Array("abcdefghijklmnopqrstuvwxyz!.".chars)
letters << "spc" << "qm"

letters.each {|letter|
	stream = File.open("#{letter}.txt", "r+")
	line = stream.gets

	# size:10|(2,1)(2,2)(2,3)(2,4)(2,5)(2,6)(2,7)(2,8)(2,9)(3,0)(3,5)(4,0)(4,5)(5,0)(5,5)(6,0)(6,5)(7,1)(7,2)(7,3)(7,4)(7,5)(7,6)(7,7)(7,8)(7,9)
	line.gsub!(/(\)\()|\)$/, ')*-65536/(')
	if (line)
		line.chomp!('(')
	end
	
	stream.seek(0);
	stream.write(line)
}