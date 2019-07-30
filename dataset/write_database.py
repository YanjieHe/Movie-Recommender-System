import os
import sys

command = "mysqlimport --ignore-lines=1 --fields-terminated-by=, --local -u root -p {0} {1}"

if __name__ == "__main__":
    if len(sys.argv) == 3:
        command = command.format(sys.argv[1], sys.argv[2])
        print(command)
        os.system(command)
    else:
        print("wrong arguments")
