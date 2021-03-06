/*	
 * Decompiled with CFR 0.141.	
 */	
package com.jeesite.common.validator;	
	
import com.jeesite.common.collect.ListUtils;	
import com.jeesite.common.collect.MapUtils;	
import com.jeesite.common.lang.StringUtils;	
import com.jeesite.common.validator.m;	
import com.jeesite.modules.msg.entity.content.BaseMsgContent;	
import com.jeesite.modules.sys.utils.ConfigUtils;	
import java.util.ArrayList;	
import java.util.HashMap;	
import java.util.Iterator;	
import java.util.List;	
import java.util.Map;	
import java.util.Set;	
import javax.validation.ConstraintViolation;	
import javax.validation.ConstraintViolationException;	
import javax.validation.Path;	
import javax.validation.Validator;	
	
public class ValidatorUtils {	
    public static Map<String, String> extractPropertyAndMessage(Set<? extends ConstraintViolation> constraintViolations) {	
        Iterator<? extends ConstraintViolation> iterator;	
        HashMap<String, String> a = MapUtils.newHashMap();	
        Iterator<? extends ConstraintViolation> iterator2 = iterator = constraintViolations.iterator();	
        while (iterator2.hasNext()) {	
            ConstraintViolation a2 = iterator.next();	
            iterator2 = iterator;	
            a.put(a2.getPropertyPath().toString(), a2.getMessage());	
        }	
        return a;	
    }	
	
    public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations) {	
        return ValidatorUtils.extractPropertyAndMessageAsList(constraintViolations, " ");	
    }	
	
    public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e2, String separator) {	
        return ValidatorUtils.extractPropertyAndMessageAsList(e2.getConstraintViolations(), separator);	
    }	
	
    public static boolean validate(StringBuilder message, Object object, Class<?> ... groups) {	
        try {	
            ValidatorUtils.validateWithException(m.ALLATORIxDEMO(), object, groups);	
        }	
        catch (ConstraintViolationException a) {	
            List<String> a2 = ValidatorUtils.extractPropertyAndMessageAsList(a, ": ");	
            if (a2.size() > 0) {	
                List<String> list = a2;	
                list.add(0, "☆");	
                message.append(StringUtils.join(list, "<br/>☆"));	
            }	
            return false;	
        }	
        return true;	
    }	
	
    public static Map<String, String> extractPropertyAndMessage(ConstraintViolationException e2) {	
        return ValidatorUtils.extractPropertyAndMessage(e2.getConstraintViolations());	
    }	
	
    public static String ALLATORIxDEMO(String s) {	
        int n = s.length();	
        int n2 = n - 1;	
        char[] arrc = new char[n];	
        int n3 = 4 << 4 ^ (3 << 2 ^ 3);	
        5 << 4 ^ 5;	
        int n4 = n2;	
        int n5 = 2 << 3 ^ (2 ^ 5);	
        while (n4 >= 0) {	
            int n6 = n2--;	
            arrc[n6] = (char)(s.charAt(n6) ^ n5);	
            if (n2 < 0) break;	
            int n7 = n2--;	
            arrc[n7] = (char)(s.charAt(n7) ^ n3);	
            n4 = n2;	
        }	
        return new String(arrc);	
    }	
	
    public static List<String> extractMessage(ConstraintViolationException e2) {	
        return ValidatorUtils.extractMessage(e2.getConstraintViolations());	
    }	
	
    /*	
     * WARNING - void declaration	
     */	
    public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations, String separator) {	
        Iterator<? extends ConstraintViolation> iterator;	
        ArrayList<String> a = ListUtils.newArrayList();	
        Iterator<? extends ConstraintViolation> iterator2 = iterator = constraintViolations.iterator();	
        while (iterator2.hasNext()) {	
            void a2;	
            ConstraintViolation constraintViolation = iterator.next();	
            iterator2 = iterator;	
            a.add(a2.getPropertyPath() + separator + a2.getMessage());	
        }	
        return a;	
    }	
	
    public static List<String> extractMessage(Set<? extends ConstraintViolation> constraintViolations) {	
        Iterator<? extends ConstraintViolation> iterator;	
        ArrayList<String> a = ListUtils.newArrayList();	
        Iterator<? extends ConstraintViolation> iterator2 = iterator = constraintViolations.iterator();	
        while (iterator2.hasNext()) {	
            ConstraintViolation a2 = iterator.next();	
            iterator2 = iterator;	
            a.add(a2.getMessage());	
        }	
        return a;	
    }	
	
    public static void validateWithException(Validator validator, Object object, Class<?> ... groups) throws ConstraintViolationException {	
        Set<ConstraintViolation<Object>> a = validator.validate(object, groups);	
        if (!a.isEmpty()) {	
            throw new ConstraintViolationException(a);	
        }	
    }	
	
    public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e2) {	
        return ValidatorUtils.extractPropertyAndMessageAsList(e2.getConstraintViolations(), " ");	
    }	
	
    public static void validateWithException(Object object, Class<?> ... groups) throws ConstraintViolationException {	
        ValidatorUtils.validateWithException(m.ALLATORIxDEMO(), object, groups);	
    }	
}	
	
