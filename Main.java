import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Parser {
    String commandName;
    String[] args;
    //This method will divide the input into commandName and args
//where "input" is the string command entered by the user
    public Parser()
    {
        args = new String[0];
        commandName="";
    }
    public boolean parse(String input)
    {
        String[] command=input.split(" ");
        if(input.contains(">>")) {
        String[] parts = input.split(Pattern.quote(">>"));
        String commandAndArgs = parts[0].trim();
        String outputFile = parts[1].trim();
        if(parse(commandAndArgs) == true) {
            String regex = "^[a-zA-Z0-9_]+\\.txt$";
            Pattern p = Pattern.compile(regex);
            Matcher matching = p.matcher(outputFile);
            if(matching.matches()) {
                String[] Secondparts = commandAndArgs.split(" ");
                commandName = ">>";
                args = new String[Secondparts.length + 1];
                System.arraycopy(Secondparts, 0, args, 0, Secondparts.length);
                args[args.length - 1] = outputFile;
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }

    else if(input.contains(">"))
        {
            String[] parts = input.split(">");
            String commandAndArgs = parts[0].trim();
            String outputFile = parts[1].trim();
            if(parse(commandAndArgs)==true)
            {
                String regex = "^[a-zA-Z0-9_]+\\.txt$";
                Pattern p = Pattern.compile(regex);
                // Create a Matcher object
                Matcher matching = p.matcher(outputFile);
                if(matching.matches())
                {
                    String[] Secondparts = commandAndArgs.split(" ");
                    commandName=">";
                    //need to modify
                    args = new String[Secondparts.length + 1];
                    // Copy elements from Secondparts to args
                    System.arraycopy(Secondparts, 0, args, 0, Secondparts.length);
                    args[args.length - 1] = outputFile;
                    return true;
                }
                else
                    return false;
            }
            else
                return false;
        }
        else if(command[0].equals("echo")) {
            if(command.length == 2) {
                commandName = command[0];
                args = Arrays.copyOfRange(command, 1, command.length);
                return true;
            } else {
                return false;
            }
        }
        else if (command[0].equals("pwd"))
        {
            if(command.length == 1) {
                commandName = command[0];
                return true;
            }
        }
        else if(command[0].equals("ls"))
        {
            if(command.length == 1) {
                commandName = command[0];
                return true;
            }
        }
        else if(command[0].equals("ls-r"))
        {
            if(command.length == 1) {
                commandName = command[0];
                return true;
            }
            else
                return false;
        }
        else if(command[0].equals("cd"))
        {
            if(command.length == 1) {
                commandName = command[0];
                return true;
            }
            else if (command.length==2)
            {
                commandName = command[0];
                args = Arrays.copyOfRange(command, 1, command.length);
                return true;
            }

            else
                return false;
        }
        else if (command[0].equals("touch"))
        {
            if(command.length == 1)
            {
                return  false;
            }
            else if (command.length > 2)
            {
                return  false;
            }
            else
            {
                commandName = command[0];
                args = Arrays.copyOfRange(command , 1 , command.length);
                return true;
            }

        }
        else if (command[0].equals("cp"))
        {
            if (command.length != 3)
            {
                return false;
            }
            File file1 = new File(command[1]);
            File file2 = new File(command[2]);
            if(file1.isFile() && file2.isFile())
            {
                commandName = command[0];
                args = Arrays.copyOfRange(command,1,command.length);
                return true;
            }
            System.out.println("one of file or both not exists or path is wrong");
            return false;
        }
        else if(command[0].equals("exit"))
        {
            if(command.length == 1)
            {
                commandName = command[0];
                return true;
            }
        }
        else if (command[0].equals("history"))
        {
            if(command.length == 1)
            {
                commandName = command[0];
                return true;
            }
        }
        else if (command[0].equals("rm"))
        {
            if(command.length == 2)
            {
                File file = new File(command[1]);
                if(file.exists() && file.isFile())
                {
                    commandName = command[0];
                    args = Arrays.copyOfRange(command , 1 , command.length);
                    return true;
                }
            }
            System.out.println("file not exists");
        }
        return false;

    }
    public String getCommandName(){
        return commandName;
    }
    public String[] getArgs(){
        return args;
    }
}

class Terminal {
    Parser parser;
    private boolean exit = false;
    private static Set<String> history;

    public Terminal(String input) {
        parser = new Parser();
        if (parser.parse(input) == true) {
            if (history == null) {
                history = new HashSet<String>();
            }
            history.add(parser.commandName);
            chooseCommandAction();
        } else {
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Invalid Command :)) \ntry again . ");
            String OInput = myObj.nextLine();
            while (parser.parse(OInput) == false) {
                System.out.println("Invalid Command :)) \ntry again . ");
                OInput = myObj.nextLine();
            }
            if (history == null) {
                history = new HashSet<String>();
            }
            history.add(parser.commandName);
            chooseCommandAction();

        }
    }

    //Implement each command in a method, for example:
    public String pwd() {
        String currentDirectory = System.getProperty("user.dir");
        return currentDirectory;
    }

    public File[] ls() {
        File Directory = new File(System.getProperty("user.dir"));
        File[] content = new File[1];
        //check existence od file and in directory format
        if (Directory.exists() && Directory.isDirectory()) {
            content = Directory.listFiles();
            Arrays.sort(content);
            return content;
        }
        return content;
    }

    public File[] ls_r() {
        File Directory = new File(System.getProperty("user.dir"));
        File[] content = new File[1];
        //check existence od file and in directory format
        if (Directory.exists() && Directory.isDirectory()) {
            content = Directory.listFiles();
            return content;
        }
        return content;
    }

    public String echo(String statement) {
        return statement;
    }

    public String cd(String Path) {
        if (Path.equals("")) {
            String userHome = System.getProperty("user.home");
            System.setProperty("user.dir", userHome);
            String currentDirectory = System.getProperty("user.dir");
            return currentDirectory;
        } else if (Path.equals("..")) {
            String CurrentDirectory = System.getProperty("user.dir");
            File parentDirectory = new File(CurrentDirectory).getParentFile();
            if (parentDirectory != null && parentDirectory.exists() && parentDirectory.isDirectory()) {
                System.setProperty("user.dir", parentDirectory.getPath());
                CurrentDirectory = System.getProperty("user.dir");
                return CurrentDirectory;

            }

        } else {
            File Directory = new File(parser.args[0]);
            //check existence of file and in directory format
            if (Directory.exists() && Directory.isDirectory()) {
                System.setProperty("user.dir", parser.args[0]);
                String currentDirectory = System.getProperty("user.dir");
                return currentDirectory;
            }
        }
        return "Could not navigate to the previous directory.";
    }

    public Set<String> history() {
        return history;
    }

    public void OPGreater() {
        try {
            File file = new File(parser.args[parser.args.length - 1]+"\n");
            FileWriter ForWrite;
            if (file.exists()) {
                // false to replace content
                ForWrite = new FileWriter(file, false);
                String content = "";
                if (parser.args[0].equals("echo")) {
                    ForWrite.write(parser.args[parser.args.length - 2]);
                }
                else if (parser.args[0].equals("pwd")) {
                    ForWrite.write(pwd()+"\n");
                }
                else if (parser.args[0].equals("ls")) {
                    File[] Path = ls();
                    for (File f : Path) {
                        ForWrite.write(f.getName() + "\n");
                    }
                }
                else if (parser.args[0].equals("ls-r")) {
                    File[] Path = ls();
                    for (int k = Path.length - 1; k >= 0; k--) {
                        ForWrite.write(Path[k].getName() + "\n");
                    }
                }
                else if (parser.args[0].equals("history"))
                {
                    int num = 1;
                    for (String command : history) {
                        ForWrite.write(num + "- " + command+"\n");
                        num++;
                    }

                }
                ForWrite.close();
            }
            else if (file.createNewFile()) {
                ForWrite = new FileWriter(file);
                if (parser.args[0].equals("echo")) {
                    ForWrite.write(parser.args[1]+"\n");
                }
                else if (parser.args[0].equals("pwd")) {
                    ForWrite.write(pwd()+"\n");
                }
                else if (parser.args[0].equals("ls")) {
                    File[] Path = ls();
                    for (File f : Path) {
                        ForWrite.write(f.getName() + "\n");
                    }
                }
                else if (parser.args[0].equals("ls-r")) {
                    File[] Path = ls();
                    for (int k = Path.length - 1; k >= 0; k--) {
                        ForWrite.write(Path[k].getName() + "\n");
                    }
                }
                else if (parser.args[0].equals("history"))
                {
                    int num = 1;
                    for (String command : history) {
                        ForWrite.write(num + "- " + command+"\n");
                        num++;
                    }

                }
                ForWrite.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OPGreater2() {
        try {
            File file = new File(parser.args[parser.args.length - 1]);
//            System.out.println("File exists: " + file.exists());  // Debugging line
            FileWriter ForWrite;
            if (file.exists()) {
                // false to replace content
                ForWrite = new FileWriter(file, true);
                String content = "";
                if (parser.args[0].equals("echo")) {
                    ForWrite.write(parser.args[parser.args.length - 2]+"\n");
                } else if (parser.args[0].equals("pwd")) {
                    ForWrite.write(pwd()+"\n");
                } else if (parser.args[0].equals("ls")) {
                    File[] Path = ls();
                    for (File f : Path) {
                        ForWrite.write(f.getName() + "\n");
                    }
                } else if (parser.args[0].equals("ls-r")) {
                    File[] Path = ls();
                    for (int k = Path.length - 1; k >= 0; k--) {
                        ForWrite.write(Path[k].getName() + "\n");
                    }
                }
                else if (parser.args[0].equals("history"))
                {
                    int num = 1;
                    for (String command : history) {
                        ForWrite.write(num + "- " + command+"\n");
                        num++;
                    }

                }
                ForWrite.close();
            } else
                System.out.println(" Sorry ! this File doesn't exist . ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void touch(String[] args) {
        File file = new File(args[0]);
        try {
            FileWriter fileWriter = new FileWriter(file);
        } catch (IOException e) {
            System.out.println("path not found ");
        }

    }

    public void rmdir(String[] args) {
        if (args[0] == "*") {
            File currentDir = new File(System.getProperty("user.dir"));
            File[] files = currentDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory() && file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
            return;
        }
        File file = new File(args[0]);
        if (file.exists() && file.listFiles().length == 0) {
            file.delete();
        }
    }

    public void cp(String[] arg) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(arg[0]);
            os = new FileOutputStream(arg[1]);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                os.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean exit() {
        return exit;
    }

    public void rm(String[] args) {
        File file = new File(args[0]);
        file.delete();
    }

    //This method will choose the suitable command method to be called
    public void chooseCommandAction() {
        if (parser.commandName.equals("echo")) {
            System.out.println(echo(parser.args[0]));
        }
        else if (parser.commandName.equals("pwd")) {
            System.out.println(pwd());
        }
        else if (parser.commandName.equals("ls")) {
            File[] content = ls();
            if (content.length != 0) {
                for (File file : content) {
                    System.out.println(file.getName());
                }
            } else
                System.out.println("Directory is not exist or not in directory format. ");

        }
        else if (parser.commandName.equals("ls-r")) {
            File[] content = ls_r();
            if (content.length != 0) {
                for (int i = content.length - 1; i >= 0; i--) {
                    System.out.println(content[i].getName());
                }
            }
            else
                System.out.println("Current directory does not exist or is not a directory.");


        }
        else if (parser.commandName.equals(">")) {
            OPGreater();
        }
        else if (parser.commandName.equals(">>")) {
            OPGreater2();
        }
        else if (parser.commandName.equals("cd")) {
            if (parser.args.length == 0) {
                System.out.println(cd(""));
            } else if (parser.args.length == 1) {
                System.out.println(cd(parser.args[0]));
            }
        }
        else if (parser.commandName.equals("touch")) {
            touch(parser.getArgs());
        }
        else if (parser.commandName.equals("cp")) {
            cp(parser.getArgs());
        }
        else if (parser.commandName.equals("rmdir")) {
            rmdir(parser.getArgs());
        }
        else if (parser.commandName.equals("exit")) {
            exit = true;
            exit();
        }
        else if (parser.commandName.equals("history")) {
            Set<String> History;
            History=history();
            int num = 1;
            for (String command : History) {
            System.out.println(num + "- " + command);
            num++;
            }

        }
        else if (parser.commandName.equals("rm")) {
            rm(parser.getArgs());
        }

    }

    public static void main(String[] args) {
        String input;
        Scanner myObj = new Scanner(System.in);// Create a Scanner object
        while (true) {
            System.out.println("Terminal : ");
            input = myObj.nextLine();
            Terminal T = new Terminal(input);
            if (T.exit()) {
                break;
            }


        }

    }
}
