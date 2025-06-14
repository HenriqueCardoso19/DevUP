import java.util.Date;

public class ProjManager extends DevUser {
    public ProjManager(String name, String email, String password) {
        super(name, email, password, "Manager");
    }

    public Project createProject(String projName, String projDescription, Date projTime) {
        return new Project(projName, projDescription, projTime);
    }

    public void addMemberToProject(DevUser user, Project project) {
        project.getTeamMembers().add(user);
        user.setProject(project);
        System.out.println("Membro '" + user.getName() + "' adicionado ao projeto '" + project.getProjName() + "'.");
    }

    public void removeMemberFromProject(DevUser user, Project project) {
        project.getTeamMembers().remove(user);
        user.setProject(null);
        System.out.println("Membro '" + user.getName() + "' removido do projeto.");
    }
}