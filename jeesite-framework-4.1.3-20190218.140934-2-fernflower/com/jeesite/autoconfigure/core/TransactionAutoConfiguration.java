package com.jeesite.autoconfigure.core;	
	
import com.atomikos.icatch.jta.UserTransactionImp;	
import com.atomikos.icatch.jta.UserTransactionManager;	
import com.jeesite.common.config.Global;	
import javax.sql.DataSource;	
import javax.transaction.TransactionManager;	
import javax.transaction.UserTransaction;	
import org.hyperic.sigar.Mem;	
import org.springframework.boot.autoconfigure.AutoConfigureAfter;	
import org.springframework.boot.autoconfigure.AutoConfigureBefore;	
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;	
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;	
import org.springframework.context.annotation.Bean;	
import org.springframework.context.annotation.Configuration;	
import org.springframework.context.annotation.DependsOn;	
import org.springframework.jdbc.datasource.DataSourceTransactionManager;	
import org.springframework.transaction.PlatformTransactionManager;	
import org.springframework.transaction.annotation.EnableTransactionManagement;	
import org.springframework.transaction.jta.JtaTransactionManager;	
	
@Configuration	
@AutoConfigureAfter({DataSourceAutoConfiguration.class})	
@AutoConfigureBefore({DataSourceTransactionManagerAutoConfiguration.class})	
@EnableTransactionManagement	
public class TransactionAutoConfiguration {	
   @Bean({"transactionManager"})	
   @ConditionalOnProperty(	
      name = {"jdbc.jta.enabled"},	
      havingValue = "false",	
      matchIfMissing = true	
   )	
   public PlatformTransactionManager transactionManager(DataSource dataSource) {	
      return new DataSourceTransactionManager(dataSource);	
   }	
	
   @Bean(	
      initMethod = "init",	
      destroyMethod = "close"	
   )	
   @ConditionalOnProperty(	
      name = {"jdbc.jta.enabled"},	
      havingValue = "true",	
      matchIfMissing = false	
   )	
   public TransactionManager atomikosTransactionManager() throws Throwable {	
      UserTransactionManager var10000 = new UserTransactionManager();	
      var10000.setForceShutdown(false);	
      return var10000;	
   }	
	
   public static String ALLATORIxDEMO(String s) {	
      int var10000 = (2 ^ 5) << 4 ^ (3 ^ 5) << 1;	
      int var10001 = 4 << 4 ^ (2 ^ 5) << 1;	
      int var10002 = (2 ^ 5) << 4 ^ 3 << 2 ^ 1;	
      int var10003 = (s = (String)s).length();	
      char[] var10004 = new char[var10003];	
      boolean var10006 = true;	
      int var5 = var10003 - 1;	
      var10003 = var10002;	
      int var3;	
      var10002 = var3 = var5;	
      char[] var1 = var10004;	
      int var4 = var10003;	
      var10001 = var10000;	
      var10000 = var10002;	
	
      for(int var2 = var10001; var10000 >= 0; var10000 = var3) {	
         var10001 = var3;	
         char var6 = s.charAt(var3);	
         --var3;	
         var1[var10001] = (char)(var6 ^ var2);	
         if (var3 < 0) {	
            break;	
         }	
	
         var10002 = var3--;	
         var1[var10002] = (char)(s.charAt(var10002) ^ var4);	
      }	
	
      return new String(var1);	
   }	
	
   @Bean({"transactionManager"})	
   @DependsOn({"userTransaction", "atomikosTransactionManager"})	
   @ConditionalOnProperty(	
      name = {"jdbc.jta.enabled"},	
      havingValue = "true",	
      matchIfMissing = false	
   )	
   public PlatformTransactionManager jtaTransactionManager(UserTransaction userTransaction, TransactionManager atomikosTransactionManager) throws Throwable {	
      return new JtaTransactionManager(userTransaction, atomikosTransactionManager);	
   }	
	
   @Bean	
   @ConditionalOnProperty(	
      name = {"jdbc.jta.enabled"},	
      havingValue = "true",	
      matchIfMissing = false	
   )	
   public UserTransaction userTransaction() throws Throwable {	
      UserTransactionImp var10000 = new UserTransactionImp();	
      var10000.setTransactionTimeout(Global.getPropertyToInteger(" dbc.jta.transactionTimeout", String.valueOf(180)));	
      return var10000;	
   }	
}	
