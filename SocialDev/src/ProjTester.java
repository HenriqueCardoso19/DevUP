public class ProjTester extends DevUser {
    public ProjTester(String name, String email, String password) {
        super(name, email, password, "Tester");
    }

    public void validateStory(UserStory story) {
        story.setStatus(StoryStatus.VALIDADA);
        System.out.println("User Story '" + story.getTitle() + "' foi VALIDADA!");
    }
}