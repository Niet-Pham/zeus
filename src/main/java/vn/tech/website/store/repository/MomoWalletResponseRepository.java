package vn.tech.website.store.repository;

import org.springframework.data.repository.CrudRepository;
import vn.tech.website.store.model.payment.momo.MomoWalletResponse;

public interface MomoWalletResponseRepository extends CrudRepository<MomoWalletResponse, Long> {
}
