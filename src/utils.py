def read_file(path: str):
	with open(path) as file:
		lines = file.readlines()
	return lines