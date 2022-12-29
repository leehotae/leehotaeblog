package com.example.demo.User.Validation;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

import com.example.demo.User.Validation.ValidationGroups.EmailNotEmptyGroup;
import com.example.demo.User.Validation.ValidationGroups.EmailPatternCheckGroup;
import com.example.demo.User.Validation.ValidationGroups.PasswordNotEmptyGroup;
import com.example.demo.User.Validation.ValidationGroups.PasswordPatternCheckGroup;
import com.example.demo.User.Validation.ValidationGroups.UsernameNotEmptyGroup;
import com.example.demo.User.Validation.ValidationGroups.UsernamePatternCheckGroup;


@GroupSequence({Default.class,EmailNotEmptyGroup.class,EmailPatternCheckGroup.class,
	UsernameNotEmptyGroup.class,
	UsernamePatternCheckGroup.class,
	PasswordNotEmptyGroup.class,PasswordPatternCheckGroup.class})
public interface ValidationSequence {

}
