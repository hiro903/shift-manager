package roster10.service;

import com.example.roster10.Staff;
import com.example.roster10.StaffMapper;
import com.example.roster10.StaffNotFoundException;
import com.example.roster10.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class StaffServiceTest {

    @InjectMocks
    StaffService staffService;

    @Mock
    StaffMapper staffMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 存在するユーザーのIDを指定したときに正常にユーザーが返されること() {
        Staff expectedStaff = new Staff(1, "erika", LocalDate.of(2000, 7, 1), "tokyo");
        doReturn(Optional.of(expectedStaff)).when(staffMapper).findById(1);

        Staff actual = staffService.findStaff(1);

        assertThat(actual).isEqualTo(expectedStaff);
        verify(staffMapper, times(1)).findById(1);
    }

    @Test
    public void スタッフの情報を渡した時に登録されること() {
        Staff staff = Staff.createStaff("Anna", LocalDate.of(2001, 1, 1), "Tokyo");
        doNothing().when(staffMapper).insert(any(Staff.class));

        Staff insertedStaff = staffService.insert(staff.getName(), staff.getDateOfBirth(), staff.getNearestStation());

        verify(staffMapper, times(1)).insert(any(Staff.class));
        assertThat(insertedStaff).isNotNull();
        assertThat(insertedStaff.getName()).isEqualTo("Anna");
        assertThat(insertedStaff.getDateOfBirth()).isEqualTo(LocalDate.of(2001, 1, 1));
        assertThat(insertedStaff.getNearestStation()).isEqualTo("Tokyo");
    }


    @Test
    public void 存在しないユーザーのIDを指定したときに例外処理されること() {
        doReturn(Optional.empty()).when(staffMapper).findById(999);

        assertThatThrownBy(() -> staffService.findStaff(999))
                .isInstanceOf(StaffNotFoundException.class)
                .hasMessage("Staff with id 999 not found");

        verify(staffMapper, times(1)).findById(999);
    }


    @Test
    public void 更新しようとするスタッフが存在しない場合に例外処理されること() {
        doReturn(Optional.empty()).when(staffMapper).findById(999);

        assertThatThrownBy(() -> staffService.updateStaff(999, "newName", LocalDate.of(2000, 1, 1), "newStation"))
                .isInstanceOf(StaffNotFoundException.class)
                .hasMessage("Staff with id 999 not found");

        verify(staffMapper, times(1)).findById(999);
        verify(staffMapper, times(0)).updateStaff(any(Staff.class));
    }


    @Test
    public void 削除しようとするスタッフが存在しない場合に例外処理されること() {
        doReturn(Optional.empty()).when(staffMapper).findById(999);

        assertThatThrownBy(() -> staffService.deleteStaffById(999))
                .isInstanceOf(StaffNotFoundException.class)
                .hasMessage("Staff with id 999 not found");

        verify(staffMapper, times(1)).findById(999);
        verify(staffMapper, times(0)).deleteById(any(Staff.class));
    }
}
