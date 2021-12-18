package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.starterbot.model.*;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Test DiscordIdController */
class DiscordIdControllerTest {
    private DiscordIdController emptyDiscordIdController;
    private DiscordIdController discordIdController;

    private NEUUser student1;
    private NEUUser student2;
    private NEUUser student3;
    private NEUUser ta1;
    private NEUUser ta2;
    private NEUUser ta3;
    private NEUUser prof1;
    private NEUUser prof2;
    private NEUUser prof3;

    private GenericRepository<NEUUser> emptyUserRepository = new InMemoryRepository<NEUUser>();
    private GenericRepository<NEUUser> userRepository = new InMemoryRepository<NEUUser>();

    /** Test statement setup before each test */
    @BeforeEach
    public void testSetup() {
        // create enough NEUUser for statement coverage and branch coverage
        student1 = new NEUUser("Student1", "nuid0001", "discordId0001");
        student2 = new NEUUser("Student2", "nuid0002", "discordId0002");
        student3 = new NEUUser("Student3", "nuid0003", "discordId0003");
        ta1 = new NEUUser("TA1", "nuidTA1", "discordIdTA1");
        ta2 = new NEUUser("TA2", "nuidTA2", "discordIdTA2");
        ta3 = new NEUUser("TA3", "nuidTA3", "discordIdTA3");
        prof1 = new NEUUser("Prof1", "nuidProf1", "discordIdProf1");
        prof2 = new NEUUser("Prof2", "nuidProf2", "discordIdProf2");
        prof3 = new NEUUser("Prof3", "nuidProf3", "discordIdProf3");

        student1.setVaccinated(true);
        student1.setSymptomatic(false);
        student2.setVaccinated(false);
        student2.setSymptomatic(true);
        student3.setVaccinated(true);
        student3.setSymptomatic(true);

        ta1.setVaccinated(true);
        ta1.setSymptomatic(false);
        ta1.setStaff(true);
        ta2.setVaccinated(false);
        ta2.setSymptomatic(true);
        ta2.setStaff(true);
        ta3.setVaccinated(true);
        ta3.setSymptomatic(true);
        ta3.setStaff(true);

        prof1.setVaccinated(true);
        prof1.setSymptomatic(false);
        prof1.setStaff(true);
        prof2.setVaccinated(false);
        prof2.setSymptomatic(true);
        prof2.setStaff(true);
        prof3.setVaccinated(true);
        prof3.setSymptomatic(true);
        prof3.setStaff(true);

        userRepository.add(student1);
        userRepository.add(student2);
        userRepository.add(student3);
        userRepository.add(ta1);
        userRepository.add(ta2);
        userRepository.add(ta3);
        userRepository.add(prof1);
        userRepository.add(prof2);
        userRepository.add(prof3);

        discordIdController = new DiscordIdController(userRepository);
        emptyDiscordIdController = new DiscordIdController(emptyUserRepository);
    }

    /** Test getNuidByDiscordId */
    @Test
    void testGetNuidByDiscordId() {
        assertEquals(discordIdController.getNuidByDiscordId("discordId0001"), "nuid0001");
        assertEquals(discordIdController.getNuidByDiscordId("discordId0002"), "nuid0002");
        assertEquals(discordIdController.getNuidByDiscordId("discordId0003"), "nuid0003");
        assertEquals(discordIdController.getNuidByDiscordId("discordIdTA1"), "nuidTA1");
        assertEquals(discordIdController.getNuidByDiscordId("discordIdTA2"), "nuidTA2");
        assertEquals(discordIdController.getNuidByDiscordId("discordIdTA3"), "nuidTA3");
        assertEquals(discordIdController.getNuidByDiscordId("discordIdProf1"), "nuidProf1");
        assertEquals(discordIdController.getNuidByDiscordId("discordIdProf2"), "nuidProf2");
        assertEquals(discordIdController.getNuidByDiscordId("discordIdProf3"), "nuidProf3");
        assertNull(discordIdController.getNuidByDiscordId("discordId"));
        assertNull(discordIdController.getNuidByDiscordId(""));
        assertNull(discordIdController.getNuidByDiscordId(" "));
        assertNull(emptyDiscordIdController.getNuidByDiscordId("discordId"));
        assertNull(emptyDiscordIdController.getNuidByDiscordId(""));
        assertNull(emptyDiscordIdController.getNuidByDiscordId(" "));
    }

    /** Test isDiscordIdRegistered */
    @Test
    void testIsDiscordIdRegistered() {
        assertTrue(discordIdController.isDiscordIdRegistered("discordId0001"));
        assertTrue(discordIdController.isDiscordIdRegistered("discordId0002"));
        assertTrue(discordIdController.isDiscordIdRegistered("discordId0003"));
        assertTrue(discordIdController.isDiscordIdRegistered("discordIdTA1"));
        assertTrue(discordIdController.isDiscordIdRegistered("discordIdTA2"));
        assertTrue(discordIdController.isDiscordIdRegistered("discordIdTA3"));
        assertTrue(discordIdController.isDiscordIdRegistered("discordIdProf1"));
        assertTrue(discordIdController.isDiscordIdRegistered("discordIdProf2"));
        assertTrue(discordIdController.isDiscordIdRegistered("discordIdProf3"));
        assertFalse(discordIdController.isDiscordIdRegistered("discordId"));
        assertFalse(discordIdController.isDiscordIdRegistered(""));
        assertFalse(discordIdController.isDiscordIdRegistered(" "));
        assertFalse(emptyDiscordIdController.isDiscordIdRegistered("discordId"));
        assertFalse(emptyDiscordIdController.isDiscordIdRegistered(""));
        assertFalse(emptyDiscordIdController.isDiscordIdRegistered(" "));
    }

    /** Test getNEUUser */
    @Test
    void testgetNEUUser() {
        assertEquals(discordIdController.getNEUUser("discordId0001"), student1);
        assertEquals(discordIdController.getNEUUser("discordId0002"), student2);
        assertEquals(discordIdController.getNEUUser("discordId0003"), student3);
        assertEquals(discordIdController.getNEUUser("discordIdTA1"), ta1);
        assertEquals(discordIdController.getNEUUser("discordIdTA2"), ta2);
        assertEquals(discordIdController.getNEUUser("discordIdTA3"), ta3);
        assertEquals(discordIdController.getNEUUser("discordIdProf1"), prof1);
        assertEquals(discordIdController.getNEUUser("discordIdProf2"), prof2);
        assertEquals(discordIdController.getNEUUser("discordIdProf3"), prof3);
        assertNotEquals(discordIdController.getNEUUser("discordId0001"), student2);
        assertNotEquals(discordIdController.getNEUUser("discordId0001"), ta2);
        assertNotEquals(discordIdController.getNEUUser("discordId0001"), prof2);
        assertNotEquals(discordIdController.getNEUUser("discordIdTA1"), student2);
        assertNotEquals(discordIdController.getNEUUser("discordIdTA1"), ta2);
        assertNotEquals(discordIdController.getNEUUser("discordIdTA1"), prof2);
        assertNotEquals(discordIdController.getNEUUser("discordIdProf1"), student2);
        assertNotEquals(discordIdController.getNEUUser("discordIdProf1"), ta2);
        assertNotEquals(discordIdController.getNEUUser("discordIdProf1"), prof2);
        assertNull(discordIdController.getNEUUser("discordId"));
        assertNull(discordIdController.getNEUUser(" "));
        assertNull(discordIdController.getNEUUser(""));
        assertNull(emptyDiscordIdController.getNEUUser("discordId0001"));
        assertNull(emptyDiscordIdController.getNEUUser(" "));
        assertNull(emptyDiscordIdController.getNEUUser(""));
    }

    /** Test createNEUUser */
    @Test
    void testCreateNEUUser() {
        NEUUser user1 = discordIdController.createNEUUser("user1", "0001", "student", "d1");
        NEUUser user2 = discordIdController.createNEUUser("user2", "0002", "ta", "d2");
        NEUUser user3 = discordIdController.createNEUUser("user3", "0003", "professor", "d2");
        NEUUser user4 = discordIdController.createNEUUser("user4", "0004", "unkownn", "d2");

        NEUUser user5 = emptyDiscordIdController.createNEUUser("user5", "0005", "student", "d5");
        NEUUser user6 = emptyDiscordIdController.createNEUUser("user6", "0006", "ta", "d6");
        NEUUser user7 = emptyDiscordIdController.createNEUUser("user7", "0007", "professor", "d7");
        NEUUser user8 = emptyDiscordIdController.createNEUUser("user8", "0008", "unkownn", "d8");

        assertEquals(discordIdController.getNEUUserByNuid("0001"), user1);
        assertEquals(discordIdController.getNEUUserByNuid("0002"), user2);
        assertEquals(discordIdController.getNEUUserByNuid("0003"), user3);
        assertEquals(discordIdController.getNEUUserByNuid("0004"), user4);
        assertFalse(user1.isStaff());
        assertTrue(user2.isStaff());
        assertTrue(user3.isStaff());
        assertThrows(
                NullPointerException.class,
                () -> {
                    assertNull(user4.isStaff());
                });

        assertEquals(emptyDiscordIdController.getNEUUserByNuid("0005"), user5);
        assertEquals(emptyDiscordIdController.getNEUUserByNuid("0006"), user6);
        assertEquals(emptyDiscordIdController.getNEUUserByNuid("0007"), user7);
        assertEquals(emptyDiscordIdController.getNEUUserByNuid("0008"), user8);
    }

    /** Test getAllTaProf */
    @Test
    void testGetAllTaProf() {
        Collection<NEUUser> TaAndProf = discordIdController.getAllTAProf();
        Collection<NEUUser> emptyTaAndProf = emptyDiscordIdController.getAllTAProf();
        assertThat(TaAndProf.size()).isEqualTo(6);
        assertThat(TaAndProf.contains(student1));
        assertThat(TaAndProf.contains(student2));
        assertThat(TaAndProf.contains(student3));
        assertThat(TaAndProf.contains(ta1));
        assertThat(TaAndProf.contains(ta2));
        assertThat(TaAndProf.contains(ta3));
        assertThat(TaAndProf.contains(prof1));
        assertThat(TaAndProf.contains(prof2));
        assertThat(TaAndProf.contains(prof3));
        assertThat(emptyTaAndProf.contains(student1)).isFalse();
        assertThat(emptyTaAndProf.contains(ta1)).isFalse();
        assertThat(emptyTaAndProf.contains(prof1)).isFalse();
        assertThat(emptyTaAndProf.size()).isEqualTo(0);
    }

    /** Test updateVaccination */
    @Test
    void testUpdateVaccination() {
        discordIdController.createNEUUser("user1", "0001", "student", "d1");
        discordIdController.createNEUUser("user2", "0002", "ta", "d2");

        assertTrue(discordIdController.updateVaccination("discordId0001", true));
        assertTrue(discordIdController.updateVaccination("discordIdTA1", true));
        assertTrue(discordIdController.updateVaccination("discordIdProf1", true));
        assertTrue(discordIdController.updateVaccination("d1", false));
        assertTrue(discordIdController.updateVaccination("d2", false));
        assertFalse(discordIdController.updateVaccination("discordId", true));
        assertFalse(emptyDiscordIdController.updateVaccination("discordId", true));
    }

    /** Test updateSymtomatic */
    @Test
    void testUpdateSymptomatic() {
        discordIdController.createNEUUser("user1", "0001", "student", "d1");
        discordIdController.createNEUUser("user2", "0002", "ta", "d2");

        assertTrue(discordIdController.updateSymptomatic("discordId0001", true));
        assertTrue(discordIdController.updateSymptomatic("discordIdTA1", true));
        assertTrue(discordIdController.updateSymptomatic("discordIdProf1", true));
        assertTrue(discordIdController.updateSymptomatic("d1", false));
        assertTrue(discordIdController.updateSymptomatic("d2", false));
        assertFalse(discordIdController.updateSymptomatic("discordId", true));
        assertFalse(emptyDiscordIdController.updateSymptomatic("discordId", true));
    }

    /** Test setInvolveOfficeHours */
    @Test
    void testSetInvolvedOfficeHours() {
        List<OfficeHour> list = new ArrayList<OfficeHour>();
        OfficeHour officeHour =
                new OfficeHour(DayOfWeek.MONDAY, new OfficeHourType("remote"), 12, 13, "1234");
        list.add(officeHour);

        assertFalse(discordIdController.setInvolvedOfficeHours("discordId", list));
        assertTrue(discordIdController.setInvolvedOfficeHours("discordId0001", list));
        assertTrue(discordIdController.setInvolvedOfficeHours("discordIdTA1", list));
        assertEquals(
                discordIdController.getNEUUser("discordId0001").getInvolvedOfficeHours().get(0),
                officeHour);
        assertFalse(emptyDiscordIdController.setInvolvedOfficeHours("discordId", list));
    }

    /** Test getNEUUserByNuid */
    @Test
    void testGetNEUUserByNuid() {
        assertEquals(discordIdController.getNEUUserByNuid("nuid0001"), student1);
        assertEquals(discordIdController.getNEUUserByNuid("nuidTA1"), ta1);
        assertEquals(discordIdController.getNEUUserByNuid("nuidProf1"), prof1);
        assertNull(discordIdController.getNEUUserByNuid("nuid"));
        assertNull(emptyDiscordIdController.getNEUUserByNuid("nuid"));
    }
}
