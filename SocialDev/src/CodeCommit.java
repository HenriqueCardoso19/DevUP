public class CodeCommit extends Artifact {
    private String commitHash;
    private String branchName;

    public CodeCommit(DevUser author, String commitHash, String branchName) {
        super(author);
        this.commitHash = commitHash;
        this.branchName = branchName;
    }

    @Override
    public String getDetails() {
        return "Commit de Código: " + commitHash + "\nAutor: " + getAuthor().getName();
    }
}