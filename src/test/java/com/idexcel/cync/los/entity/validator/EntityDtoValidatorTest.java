package com.idexcel.cync.los.entity.validator;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idexcel.cync.los.entity.dto.CommercialEntityDto;
import com.idexcel.cync.los.entity.dto.IndividualEntityDto;
import com.idexcel.cync.los.entity.exception.BadRequestException;

@RunWith(SpringJUnit4ClassRunner.class)
public class EntityDtoValidatorTest {

	@InjectMocks
	public EntityDtoValidator validator;

	@MockBean
	MessageSource errorMsgs;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		this.validator = new EntityDtoValidator(errorMsgs);
	}

	@Test
	public void isvalidDateNullCheckCommercialEntityDtoTest() {
		CommercialEntityDto commercialEntityDto = new CommercialEntityDto();
		commercialEntityDto.setBusinessOpenDate(null);
		validator.isvalidDate(commercialEntityDto);
		assertNull(null);
	}

	@Test
	public void isvalidDateCommercialEntityDtoTest() {
		CommercialEntityDto commercialEntityDto = new CommercialEntityDto();
		commercialEntityDto.setBusinessOpenDate("12/30/2019");
		validator.isvalidDate(commercialEntityDto);
		assertNull(null);
	}

	@Test
	public void isvalidDateNullCheckIndividualEntityDtoTest() {
		IndividualEntityDto individualEntityDto = new IndividualEntityDto();
		individualEntityDto.setDateOfBirth(null);
		validator.isvalidDate(individualEntityDto);
		assertNull(null);
	}

	@Test
	public void isvalidDateIndividualEntityDtoTest() {
		IndividualEntityDto individualEntityDto = new IndividualEntityDto();
		individualEntityDto.setDateOfBirth("12/30/2019");
		validator.isvalidDate(individualEntityDto);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isvalidDateFormatTest() {
		CommercialEntityDto commercialEntityDto = new CommercialEntityDto();
		commercialEntityDto.setBusinessOpenDate("02/31/2019");
		validator.isvalidDate(commercialEntityDto);
		assertNull(null);
	}

	@Test
	public void isvalidDateFormatCorrectValueTest() {
		CommercialEntityDto commercialEntityDto = new CommercialEntityDto();
		commercialEntityDto.setBusinessOpenDate("02/28/2019");
		validator.isvalidDate(commercialEntityDto);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isvalidBusinessNameLessThanThreeCaharTest() {
		CommercialEntityDto commercialEntityDto = new CommercialEntityDto();
		commercialEntityDto.setBusinessName("Te");
		validator.isvalidBusinessName(commercialEntityDto);
		assertNull(null);
	}

	@Test
	public void isvalidBusinessNameInvalidPatternTest() {
		CommercialEntityDto commercialEntityDto = new CommercialEntityDto();
		commercialEntityDto.setBusinessName("   Test");
		validator.isvalidBusinessName(commercialEntityDto);
		assertNull(null);
	}

	@Test
	public void isvalidBusinessNameTest() {
		CommercialEntityDto commercialEntityDto = new CommercialEntityDto();
		commercialEntityDto.setBusinessName(" 'Test # 9");
		validator.isvalidBusinessName(commercialEntityDto);
		assertNull(null);
	}

	@Test
	public void isvalidBusinessNameEmptyStringTest() {
		CommercialEntityDto commercialEntityDto = new CommercialEntityDto();
		commercialEntityDto.setBusinessName("");
		validator.isvalidBusinessName(commercialEntityDto);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isvalidBusinessNameOnlySpaceTest() {
		CommercialEntityDto commercialEntityDto = new CommercialEntityDto();
		commercialEntityDto.setBusinessName(" ");
		validator.isvalidBusinessName(commercialEntityDto);
		assertNull(null);
	}

}
